package com.example.store.service;

import com.example.store.mapper.AdminMapper;
import com.example.store.model.Product;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminMapper adminMapper;

    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    /**
     * 管理员端添加商品
     * @param json 前端请求json(商品信息)
     * @return 返回商品添加状态
     */
    public String addProduct(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Product product = new Product();
        product.setProduct_intro(jsonObject.get("product_intro").getAsString());
        product.setProduct_name(jsonObject.get("product_name").getAsString());
        product.setProduct_num(jsonObject.get("product_num").getAsInt());
        product.setProduct_picture(jsonObject.get("product_picture").getAsString());
        product.setProduct_price(jsonObject.get("product_price").getAsBigDecimal());
        product.setProduct_sales(jsonObject.get("product_sales").getAsInt());
        product.setProduct_selling_price(jsonObject.get("product_selling_price").getAsBigDecimal());
        product.setProduct_title(jsonObject.get("product_title").getAsString());
        product.setCategory_id(jsonObject.get("category_id").getAsInt());

        int insertRows = adminMapper.addProduct(product);
        JsonObject responseJson = new JsonObject();
        if (insertRows == 0) {
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
        Product product = new Product();
        product.setProduct_id(jsonObject.get("product_id").getAsInt());
        product.setProduct_name(jsonObject.get("product_name").getAsString());
        product.setProduct_num(jsonObject.get("product_num").getAsInt());
        product.setProduct_picture(jsonObject.get("product_picture").getAsString());
        product.setProduct_price(jsonObject.get("product_price").getAsBigDecimal());
        product.setProduct_intro(jsonObject.get("product_intro").getAsString());
        product.setProduct_sales(jsonObject.get("product_sales").getAsInt());
        product.setProduct_selling_price(jsonObject.get("product_selling_price").getAsBigDecimal());
        product.setProduct_title(jsonObject.get("product_title").getAsString());
        product.setCategory_id(jsonObject.get("category_id").getAsInt());

        int updateRows = adminMapper.updateProduct(product);
        JsonObject responseJson = new JsonObject();
        if (updateRows == 0) {
            responseJson.add("status", new JsonPrimitive(-1));
            responseJson.add("message", new JsonPrimitive("修改商品信息失败"));
            return responseJson.toString();
        }
        responseJson.add("status", new JsonPrimitive(0));
        responseJson.add("message", new JsonPrimitive("修改商品信息成功"));
        return responseJson.toString();
    }

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
}
