package com.example.store.service;

import com.example.store.mapper.ProductMapper;
import com.example.store.model.Category;
import com.example.store.model.Product;
import com.example.store.model.ProductPicture;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
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
        List<Product> productList = productMapper.getAllProduct();

        PageInfo<Product> pageInfo = new PageInfo<>(productList);

        Gson gson = new Gson();

        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("Product", gson.toJsonTree(productList).getAsJsonArray());
        responseJson.add("total", new JsonPrimitive(pageInfo.getTotal()));

        return responseJson.toString();
    }

    /**
     * 根据商品分类名称获取首页展示的商品信息
     * @param json 前端请求json(分类名称)
     * @return 返回商品信息
     */
    public String getPromoProduct(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String categoryName = jsonObject.get("categoryName").getAsString();

        Category category = productMapper.getCategoryByName(categoryName);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        List<Product> productList = productMapper.getPromoProductByCategoryId(categoryList);

        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("Product", gson.toJsonTree(productList));

        return responseJson.toString();
    }

    /**
     * 根据商品分类名称获取热门商品信息
     * @param json 前端请求json(分类名称)
     * @return 返回热门商品信息
     */
    public String getHotProduct(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        JsonArray categoryNameArray = jsonObject.get("categoryName").getAsJsonArray();
        List<String> categoryNameList = new ArrayList<>();
        categoryNameArray.forEach(categoryNameArrayElement -> categoryNameList.add(categoryNameArrayElement.getAsString()));

        List<Category> categoryList = new ArrayList<>();
        for (String categoryName : categoryNameList) {
            categoryList.add(productMapper.getCategoryByName(categoryName));
        }

        List<Product> productList = new ArrayList<>(productMapper.getPromoProductByCategoryId(categoryList));

        List<Product> newProductList = new ArrayList<>(Collections.nCopies(7, null));

        List<Integer> randomArray = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            randomArray.add(i);
        }

        for (int i = 0; i < productList.size(); i++){
            int iRandNum = (int)(Math.random() * productList.size());
            int temp = randomArray.get(iRandNum);
            randomArray.set(iRandNum, randomArray.get(i));
            randomArray.set(i, temp);
        }

        for (int i = 0; i < productList.size(); i++) {
            newProductList.set(randomArray.get(i), productList.get(i));
        }

        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("Product", gson.toJsonTree(newProductList));

        return responseJson.toString();
    }

    /**
     * 根据分类id,分页获取商品信息
     * @param json 前端请求json(分类id,页码,偏移量)
     * @return 商品信息
     */
    public String getProductByCategory(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        int currentPage = jsonObject.get("currentPage").getAsInt();
        int pageSize = jsonObject.get("pageSize").getAsInt();
        // int offset = (currentPage - 1) * pageSize;

        List<Integer> categoryIdList = new ArrayList<>();
        if (jsonObject.get("categoryID").isJsonArray()) {
            JsonArray categoryIdArray = jsonObject.get("categoryID").getAsJsonArray();
            for (JsonElement jsonElement : categoryIdArray) {
                Integer categoryID = jsonElement.getAsInt();
                categoryIdList.add(categoryID);
            }
        }
        if (jsonObject.get("categoryID").isJsonPrimitive()) {
            int categoryId = jsonObject.get("categoryID").getAsInt();
            categoryIdList.add(categoryId);
        }

        PageHelper.startPage(currentPage, pageSize);
        List<Product> productList = productMapper.getProductByCategoryIdList(categoryIdList);

        PageInfo<Product> pageInfo = new PageInfo<>(productList);

        Gson gson = new Gson();

        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("Product", gson.toJsonTree(productList).getAsJsonArray());
        responseJson.add("total", new JsonPrimitive(pageInfo.getTotal()));

        return responseJson.toString();
    }

    /**
     * 获取商品分类
     * @return 分类信息
     */
    public String getCategory() {
        List<Category> categoryList = productMapper.getCategory();
        Gson gson = new Gson();

        JsonObject responseJson = new JsonObject();
        responseJson.add("code" ,new JsonPrimitive("001"));
        responseJson.add("category", gson.toJsonTree(categoryList).getAsJsonArray());

        return responseJson.toString();
    }

    /**
     * 根据搜索条件,分页获取商品信息
     * @param json 前端请求json(搜索内容,页码,偏移量)
     * @return 返回相应的搜索结果
     */
    public String getProductBySearch(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        int currentPage = jsonObject.get("currentPage").getAsInt();
        int pageSize = jsonObject.get("pageSize").getAsInt();
        // int offset = (currentPage - 1) * pageSize;
        String search = jsonObject.get("search").getAsString();

        List<Category> categoryList = productMapper.getCategory();

        // 如果搜索内容为分类，则直接返回该分类下的商品信息
        for (Category category : categoryList) {
            if (search.equals(category.getCategory_name())) {
                int categoryId = category.getCategory_id();

                PageHelper.startPage(currentPage, pageSize);
                List<Product> productList = productMapper.getProductByCategoryId(categoryId);

                PageInfo<Product> pageInfo = new PageInfo<>(productList);

                Gson gson = new Gson();
                JsonObject responseJson = new JsonObject();
                responseJson.add("code", new JsonPrimitive("001"));
                responseJson.add("Product", gson.toJsonTree(productList).getAsJsonArray());
                responseJson.add("total", new JsonPrimitive(pageInfo.getTotal()));

                return responseJson.toString();
            }
        }

        // 否则返回根据查询条件模糊查询的商品分页结果
        PageHelper.startPage(currentPage, pageSize);
        List<Product> productList = productMapper.getProductBySearch(search);

        PageInfo<Product> pageInfo = new PageInfo<>(productList);

        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("Product", gson.toJsonTree(productList).getAsJsonArray());
        responseJson.add("total", new JsonPrimitive(pageInfo.getTotal()));

        return responseJson.toString();

    }

    /**
     * 根据商品id,获取商品详细信息
     * @param json 前端请求json(商品id)
     * @return 商品信息
     */
    public String getDetails(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int productId = jsonObject.get("productID").getAsInt();

        Product product = productMapper.getProductById(productId);
        if (product == null) {
            return null;
        }

        List<Product> tempProductList = new ArrayList<>();
        tempProductList.add(product);
        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("Product", new Gson().toJsonTree(tempProductList).getAsJsonArray());

        return responseJson.toString();
    }

    /**
     * 根据商品id,获取商品图片,用于商品详情的页面展示
     * @param json 前端请求json(商品id)
     * @return 商品图片路径
     */
    public String getDetailsPicture(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int productId = jsonObject.get("productID").getAsInt();

        List<ProductPicture> productPictureList = productMapper.getDetailsPictureByProductId(productId);
        if (productPictureList.isEmpty()) {
            return null;
        }

        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("ProductPicture", gson.toJsonTree(productPictureList).getAsJsonArray());

        return responseJson.toString();
    }



}
