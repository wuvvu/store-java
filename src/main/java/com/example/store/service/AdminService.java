package com.example.store.service;

import com.example.store.mapper.AdminMapper;
import com.example.store.mapper.ProductMapper;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final AdminMapper adminMapper;

    private final ProductMapper productMapper;

    public AdminService(AdminMapper adminMapper, ProductMapper productMapper) {
        this.adminMapper = adminMapper;
        this.productMapper = productMapper;
    }

    /**
     * 管理员端添加商品
     * @param json 前端请求json(商品信息)
     * @return 返回商品添加状态
     */
    public String addProduct(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        List<String> pictureList = new ArrayList<>();
        JsonArray pictureArray = jsonObject.get("product_picture").getAsJsonArray();
        for (JsonElement jsonElement : pictureArray) {
            String pictureString = jsonElement.getAsString();
            pictureList.add(pictureString);
        }
        Product product = new Product();
        product.setProduct_id(0);
        product.setProduct_intro(jsonObject.get("product_intro").getAsString());
        product.setProduct_name(jsonObject.get("product_name").getAsString());
        product.setProduct_num(jsonObject.get("product_num").getAsInt());
        product.setProduct_picture(pictureList.get(0));
        product.setProduct_price(jsonObject.get("product_price").getAsBigDecimal());
        product.setProduct_sales(0);
        product.setProduct_selling_price(jsonObject.get("product_selling_price").getAsBigDecimal());
        product.setProduct_title(jsonObject.get("product_title").getAsString());
        product.setCategory_id(jsonObject.get("category_id").getAsInt());

        int insertRows = adminMapper.addProduct(product);

        int insertPictureRows = adminMapper.addProductPicture(product.getProduct_id(), pictureList);

        JsonObject responseJson = new JsonObject();
        if (insertRows == 0 || insertPictureRows == 0) {
            responseJson.add("status", new JsonPrimitive(-1));
            responseJson.add("message", new JsonPrimitive("添加商品失败"));
            return responseJson.toString();
        }
        responseJson.add("status", new JsonPrimitive(0));
        responseJson.add("message", new JsonPrimitive("添加商品成功"));
        return responseJson.toString();
    }

    /**
     * 管理员端下架商品
     * @param json 前端请求json(商品id)
     * @return 商品下架状态
     */
    public String offShelf(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int productId = jsonObject.get("product_id").getAsInt();
        int deleteRows = adminMapper.offShelfById(productId);

        JsonObject responseJson = new JsonObject();
        if (deleteRows == 0) {
            responseJson.add("status", new JsonPrimitive(-1));
            responseJson.add("message", new JsonPrimitive("下架商品失败"));
            return responseJson.toString();
        }
        responseJson.add("status", new JsonPrimitive(0));
        responseJson.add("message", new JsonPrimitive("下架商品成功"));
        return responseJson.toString();
    }

    /**
     * 修改商品信息
     * @param json 前端请求json(商品信息)
     * @return 返回商品修改状态
     */
    public String updateProduct(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        List<String> pictureList = new ArrayList<>();
        JsonArray pictureArray = jsonObject.get("product_picture").getAsJsonArray();
        for (JsonElement jsonElement : pictureArray) {
            String pictureString = jsonElement.getAsString();
            pictureList.add(pictureString);
        }
        Product product = new Product();
        product.setProduct_id(jsonObject.get("product_id").getAsInt());
        product.setProduct_name(jsonObject.get("product_name").getAsString());
        product.setProduct_num(jsonObject.get("product_num").getAsInt());
        product.setProduct_picture(pictureList.get(0));
        product.setProduct_price(jsonObject.get("product_price").getAsBigDecimal());
        product.setProduct_intro(jsonObject.get("product_intro").getAsString());
        product.setProduct_selling_price(jsonObject.get("product_selling_price").getAsBigDecimal());
        product.setProduct_title(jsonObject.get("product_title").getAsString());
        product.setCategory_id(jsonObject.get("category_id").getAsInt());

        int updateRows = adminMapper.updateProduct(product);
        int deletePictures = adminMapper.deletePictureByProductId(product.getProduct_id());
        int insertPictureRows = adminMapper.addProductPicture(product.getProduct_id(), pictureList);

        JsonObject responseJson = new JsonObject();
        if (updateRows == 0 || deletePictures == 0 ||insertPictureRows == 0) {
            responseJson.add("status", new JsonPrimitive(-1));
            responseJson.add("message", new JsonPrimitive("修改商品信息失败"));
            return responseJson.toString();
        }
        responseJson.add("status", new JsonPrimitive(0));
        responseJson.add("message", new JsonPrimitive("修改商品信息成功"));
        return responseJson.toString();
    }

    /**
     * 管理员端上架商品
     * @param json 前端请求json(商品id)
     * @return 商品上架状态
     */
    public String onShelf(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int productId = jsonObject.get("product_id").getAsInt();
        int deleteRows = adminMapper.onShelfById(productId);

        JsonObject responseJson = new JsonObject();
        if (deleteRows == 0) {
            responseJson.add("status", new JsonPrimitive(-1));
            responseJson.add("message", new JsonPrimitive("上架商品失败"));
            return responseJson.toString();
        }
        responseJson.add("status", new JsonPrimitive(0));
        responseJson.add("message", new JsonPrimitive("上架商品成功"));
        return responseJson.toString();
    }

    /**
     * 分页获取所有的商品信息
     * @param json 前端请求json(页码、偏移量)
     * @return 商品信息、总数
     */
    public String getAllProduct(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        int currentPage = jsonObject.get("currentPage").getAsInt();
        int pageSize = jsonObject.get("pageSize").getAsInt();

        // int offset = (currentPage - 1) * pageSize;

        PageHelper.startPage(currentPage, pageSize);
        List<Product> productList = adminMapper.getAllProduct();

        PageInfo<Product> pageInfo = new PageInfo<>(productList);

        Gson gson = new Gson();

        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("Product", gson.toJsonTree(productList).getAsJsonArray());
        responseJson.add("total", new JsonPrimitive(pageInfo.getTotal()));

        return responseJson.toString();
    }

    /**
     * 管理员端获取订单信息
     * @param json 前端请求json(页码、偏移量、搜索)
     * @return 订单信息
     */
    public String getOrder(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        int currentPage = jsonObject.get("currentPage").getAsInt();
        int pageSize = jsonObject.get("pageSize").getAsInt();

        String search = jsonObject.get("search").getAsString();

        PageHelper.startPage(currentPage, pageSize);
        List<Order> orderIdList = adminMapper.getOrderIdBySearch(search);

        List<Order> orderList = adminMapper.getOrderBySearch(search);

        List<List<Order>> ordersList = new ArrayList<>();

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
            ordersList.add(tempOrder);
        }

        JsonObject responseJson = new JsonObject();
        responseJson.add("status", new JsonPrimitive(0));
        responseJson.add("orders", new Gson().toJsonTree(ordersList).getAsJsonArray());
        return responseJson.toString();
    }
}
