package com.example.store.service;

import com.example.store.mapper.OrderMapper;
import com.example.store.mapper.ProductMapper;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.model.ShoppingCart;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderMapper orderMapper;

    private final ProductMapper productMapper;

    public OrderService(OrderMapper orderMapper, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
    }

    /**
     * 获取用户所有订单信息
     * @param json 前端请求json(用户id)
     * @return 返回订单信息json
     */
    public String getOrder(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();
        List<Order> orderIdList = orderMapper.getOrderIdByUserId(userId);

        JsonObject responseJson = new JsonObject();
        if (orderIdList.isEmpty()) {
            responseJson.add("code", new JsonPrimitive("002"));
            responseJson.add("msg", new JsonPrimitive("该用户没有订单信息"));
            return responseJson.toString();
        }

        List<Order> orderList = orderMapper.getOrderByUserId(userId);
        List<Order> ordersList = new ArrayList<>();

        for (Order orderForId : orderIdList) {
            long orderId = orderForId.getOrder_id();
            List<Order> tempOrder = new ArrayList<>();

            for (Order order : orderList) {
                if (orderId == order.getOrder_id()) {
                    Product product = productMapper.getProductById(order.getProduct_id());
                    order.setProduct_name(product.getProduct_name());
                    order.setProduct_picture(product.getProduct_picture());

                    tempOrder.add(order);
                }
            }
            ordersList.addAll(tempOrder);
        }

        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("orders", new Gson().toJsonTree(ordersList).getAsJsonArray());
        return responseJson.toString();
    }

    /**
     * 添加用户订单信息
     * @param json 前端请求json(用户id,商品id)
     * @return 返回订单信息修改状态
     */
    public String addOrder(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();
        JsonArray products = jsonObject.get("products").getAsJsonArray();
        List<Order> orderList = new ArrayList<>();

        long timestamp = System.currentTimeMillis();

        for (JsonElement productJsonElement : products) {
            JsonObject productJsonObject = productJsonElement.getAsJsonObject();
            Order order = new Order();
            order.setOrder_id(Long.parseLong("" + userId + timestamp));
            order.setUser_id(userId);
            order.setProduct_id(productJsonObject.get("productID").getAsInt());
            order.setProduct_num(productJsonObject.get("num").getAsInt());
            order.setProduct_price((productJsonObject.get("price").getAsBigDecimal()));
            order.setOrder_time(timestamp);

            orderList.add(order);
        }

        int insertRows = orderMapper.addOrder(orderList);

        JsonObject responseJson = new JsonObject();

        // 插入成功
        if (insertRows == orderList.size()) {
            int rows = 0;
            for (JsonElement productJsonElement : products) {
                JsonObject productJsonObject = productJsonElement.getAsJsonObject();
                int productId = productJsonObject.get("productID").getAsInt();
                int deleteRows = orderMapper.deleteShoppingCart(userId, productId);
                rows += deleteRows;
            }

            if (rows != orderList.size()) {
                responseJson.add("code", new JsonPrimitive("002"));
                responseJson.add("msg", new JsonPrimitive("购买成功,但购物车没有更新成功"));
                return responseJson.toString();
            }

            responseJson.add("code", new JsonPrimitive("001"));
            responseJson.add("msg", new JsonPrimitive("购买成功"));
            return responseJson.toString();

        }

        responseJson.add("code", new JsonPrimitive("004"));
        responseJson.add("msg", new JsonPrimitive("购买失败，未知原因"));
        return responseJson.toString();
    }

    /**
     * 生成购物车详细信息
     * @param shoppingCartList 购物车原始信息
     * @return 返回详细信息
     */
    public List<ShoppingCart> parseShoppingCartData(List<ShoppingCart> shoppingCartList) {
        List<ShoppingCart> shoppingCartData = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            Product product = productMapper.getProductById(shoppingCart.getProduct_id());
            ShoppingCart shoppingCartParse = new ShoppingCart();
            shoppingCartParse.setId(shoppingCart.getId());
            shoppingCartParse.setProduct_id(shoppingCart.getProduct_id());
            shoppingCartParse.setProductName(product.getProduct_name());
            shoppingCartParse.setProductImg(product.getProduct_picture());
            shoppingCartParse.setProduct_price(product.getProduct_price());
            shoppingCartParse.setNum(shoppingCart.getNum());
            shoppingCartParse.setCheck(false);
            shoppingCartData.add(shoppingCartParse);
        }
        return shoppingCartData;
    }

    /**
     * 获取购物车信息
     * @param json 前端请求json(用户id)
     * @return 返回购物车详细信息
     */
    public String getShoppingCart(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();

        List<ShoppingCart> shoppingCartList = orderMapper.getShoppingCartByUserId(userId);

        List<ShoppingCart> shoppingCartData = parseShoppingCartData(shoppingCartList);

        JsonObject responseJson = new JsonObject();

        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("shoppingCartData", new Gson().toJsonTree(shoppingCartData).getAsJsonArray());

        return responseJson.toString();
    }

    /**
     * 插入购物车信息
     * @param json 前端请求json(用户id,商品id)
     * @return 返回购物车添加状态
     */
    public String addShoppingCart(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();
        int productId = jsonObject.get("product_id").getAsInt();
        JsonObject responseJson = new JsonObject();
        ShoppingCart tempShoppingCart = orderMapper.getShoppingCartByUserIdAndProductId(userId, productId);
        if (tempShoppingCart != null) {
            //如果存在则把数量+1
            int tempNum = tempShoppingCart.getNum() + 1;
            Product product = productMapper.getProductById(tempShoppingCart.getProduct_id());
            int maxNum = (int) Math.floor(product.getProduct_num() / 2.0);
            if (tempNum > maxNum) {
                responseJson.add("code", new JsonPrimitive("003"));
                responseJson.add("msg", new JsonPrimitive("达到限购数量 " + maxNum));
                return responseJson.toString();
            }
            // 更新购物车信息,把数量+1
            int updateRows = orderMapper.updateShoppingCart(tempNum, userId, productId);
            if (updateRows == 1) {
                responseJson.add("code", new JsonPrimitive("002"));
                responseJson.add("msg", new JsonPrimitive("该商品已在购物车，数量 +1"));
                return responseJson.toString();
            }
        }
        //不存在则添加
        int insertRows = orderMapper.addShoppingCart(userId, productId);
        if (insertRows == 1) {
            // 如果成功,获取该商品的购物车信息
            ShoppingCart shoppingCart = orderMapper.getShoppingCartByUserIdAndProductId(userId, productId);
            // 生成购物车详细信息
            List<ShoppingCart> tempShoppingCartList = new ArrayList<>();
            tempShoppingCartList.add(shoppingCart);
            List<ShoppingCart> shoppingCartData = parseShoppingCartData(tempShoppingCartList);
            responseJson.add("code", new JsonPrimitive("001"));
            responseJson.add("msg", new JsonPrimitive("添加购物车成功"));
            responseJson.add("shoppingCartData", new Gson().toJsonTree(shoppingCartData).getAsJsonArray());
            return responseJson.toString();
        }
        responseJson.add("code", new JsonPrimitive("005"));
        responseJson.add("msg", new JsonPrimitive("添加购物车失败,未知错误"));
        return responseJson.toString();
    }

    /**
     * 删除购物车信息
     * @param json 前端请求json(用户id,商品id)
     * @return 返回购物车删除状态
     */
    public String deleteShoppingCart(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();
        int productId = jsonObject.get("product_id").getAsInt();
        JsonObject responseJson = new JsonObject();

        ShoppingCart shoppingCart = orderMapper.getShoppingCartByUserIdAndProductId(userId, productId);
        if (shoppingCart != null) {
            int deleteRows = orderMapper.deleteShoppingCart(userId, productId);
            if (deleteRows > 0) {
                responseJson.add("code", new JsonPrimitive("001"));
                responseJson.add("msg", new JsonPrimitive("删除购物车成功"));
                return responseJson.toString();
            }
        }
        responseJson.add("code", new JsonPrimitive("002"));
        responseJson.add("msg", new JsonPrimitive("该商品不在购物车"));
        return responseJson.toString();

    }

    /**
     * 更新购物车商品数量
     * @param json 前端请求json(商品数量,用户id,商品id)
     * @return 返回购物车更新状态
     */
    public String updateShoppingCart(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();
        int productId = jsonObject.get("product_id").getAsInt();
        int num = jsonObject.get("num").getAsInt();

        JsonObject responseJson = new JsonObject();
        if (num < 1) {
            responseJson.add("code", new JsonPrimitive("004"));
            responseJson.add("msg", new JsonPrimitive("数量不合法"));
            return responseJson.toString();
        }

        ShoppingCart tempShoppingCart = orderMapper.getShoppingCartByUserIdAndProductId(userId, productId);
        if (tempShoppingCart == null) {
            responseJson.add("code", new JsonPrimitive("002"));
            responseJson.add("msg", new JsonPrimitive("该商品不在购物车"));
            return responseJson.toString();
        }

        if (num == tempShoppingCart.getNum()) {
            responseJson.add("code", new JsonPrimitive("003"));
            responseJson.add("msg", new JsonPrimitive("数量没有发生变化"));
            return responseJson.toString();
        }

        Product product = productMapper.getProductById(productId);
        int maxNum = (int) Math.floor(product.getProduct_num() / 2.0);
        if (num > maxNum) {
            responseJson.add("code", new JsonPrimitive("004"));
            responseJson.add("msg", new JsonPrimitive("数量达到限购数量" + maxNum));
            return responseJson.toString();
        }

        int updateRows = orderMapper.updateShoppingCart(num, userId, productId);
        if (updateRows > 0) {
            responseJson.add("code", new JsonPrimitive("001"));
            responseJson.add("msg", new JsonPrimitive("修改购物车数量成功"));
            return responseJson.toString();
        }
        responseJson.add("code", new JsonPrimitive("005"));
        responseJson.add("msg", new JsonPrimitive("修改购物车数量失败, 未知错误"));
        return responseJson.toString();
    }
}
