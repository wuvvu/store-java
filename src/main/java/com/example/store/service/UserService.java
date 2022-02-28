package com.example.store.service;

import com.example.store.mapper.ProductMapper;
import com.example.store.mapper.UserMapper;
import com.example.store.model.Collect;
import com.example.store.model.Product;
import com.example.store.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final ProductMapper productMapper;

    public UserService(UserMapper userMapper, ProductMapper productMapper) {
        this.userMapper = userMapper;
        this.productMapper = productMapper;
    }

    String usernameRule = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    String passwordRule = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 登录
     * @param json 前端传来的用户名和密码
     * @return 登录成功或失败信息
     */
    public String login(String json, HttpSession session) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String username = jsonObject.get("userName").getAsString();
        String password = jsonObject.get("password").getAsString();

        JsonObject responseJson = new JsonObject();
        if (username.isEmpty() || password.isEmpty()) {
            responseJson.add("code", new JsonPrimitive("002"));
            responseJson.add("msg", new JsonPrimitive("用户名或密码不能为空"));
            return responseJson.toString();
        }

        // 用户校验规则
        if (!username.matches(usernameRule)) {
            responseJson.add("code", new JsonPrimitive("003"));
            responseJson.add("msg", new JsonPrimitive("用户名不合法(以字母开头，允许5-16字节，允许字母数字下划线)"));
            return responseJson.toString();
        }

        // 密码校验规则
        if (!password.matches(passwordRule)) {
            responseJson.add("code", new JsonPrimitive("003"));
            responseJson.add("msg", new JsonPrimitive("密码不合法(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)"));
            return responseJson.toString();
        }

        User user = userMapper.getUserByUsernameAndPassword(username,password);

        if (user == null) {
            responseJson.add("code", new JsonPrimitive("004"));
            responseJson.add("msg", new JsonPrimitive("用户名或密码错误"));
            return responseJson.toString();
        }
        session.setAttribute("user", user);

        Gson gson = new Gson();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("msg", new JsonPrimitive("登录成功"));
        responseJson.add("user", gson.toJsonTree(user));

        return responseJson.toString();
    }

    /**
     * 查询是否存在某个用户名,用于注册时前端校验
     * @param json 前端请求json(用户名)
     * @return 用户信息
     */
    public String findUserName(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String username = jsonObject.get("userName").getAsString();

        JsonObject responseJson = new JsonObject();
        if (username.isEmpty()) {
            responseJson.add("code", new JsonPrimitive("002"));
            responseJson.add("msg", new JsonPrimitive("用户名不能为空"));
            return responseJson.toString();
        }

        if (!username.matches(usernameRule)) {
            responseJson.add("code", new JsonPrimitive("003"));
            responseJson.add("msg", new JsonPrimitive("用户名不合法(以字母开头，允许5-16字节，允许字母数字下划线)"));
            return responseJson.toString();
        }

        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            responseJson.add("code", new JsonPrimitive("001"));
            responseJson.add("msg", new JsonPrimitive("用户名不存在，可以注册"));
            return responseJson.toString();
        }

        responseJson.add("code", new JsonPrimitive("004"));
        responseJson.add("msg", new JsonPrimitive("用户名已存在，不能注册"));
        return responseJson.toString();

    }

    /**
     * 注册
     * @param json 前端传来的用户名和密码
     * @return 注册成功或失败信息
     */
    public String register(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String username = jsonObject.get("userName").getAsString();
        String password = jsonObject.get("password").getAsString();

        JsonObject responseJson = new JsonObject();
        if (username.isEmpty() || password.isEmpty()) {
            responseJson.add("code", new JsonPrimitive("002"));
            responseJson.add("msg", new JsonPrimitive("用户名或密码不能为空"));
            return responseJson.toString();
        }

        // 用户校验规则
        if (!username.matches(usernameRule)) {
            responseJson.add("code", new JsonPrimitive("003"));
            responseJson.add("msg", new JsonPrimitive("用户名不合法(以字母开头，允许5-16字节，允许字母数字下划线)"));
            return responseJson.toString();
        }

        // 密码校验规则
        if (!password.matches(passwordRule)) {
            responseJson.add("code", new JsonPrimitive("003"));
            responseJson.add("msg", new JsonPrimitive("密码不合法(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)"));
            return responseJson.toString();
        }

        User user = userMapper.getUserByUsername(username);
        if (user != null) {
            responseJson.add("code", new JsonPrimitive("004"));
            responseJson.add("msg", new JsonPrimitive("用户名已存在，不能注册"));
            return responseJson.toString();
        }

        int registerResult = userMapper.register(username, password);

        if (registerResult == 1) {
            responseJson.add("code", new JsonPrimitive("001"));
            responseJson.add("msg", new JsonPrimitive("注册成功"));
            return responseJson.toString();
        }

        responseJson.add("code", new JsonPrimitive("500"));
        responseJson.add("msg", new JsonPrimitive("未知错误，注册失败"));
        return responseJson.toString();
    }

    /**
     * 添加收藏
     * @param json 前端请求json(用户id,商品id)
     * @return 返回收藏添加状态
     */
    public String addCollect(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();
        int productId = jsonObject.get("product_id").getAsInt();

        Collect collect = userMapper.getCollectByUserIdAndProductId(userId, productId);
        JsonObject responseJson = new JsonObject();
        if (collect != null) {
            responseJson.add("code", new JsonPrimitive("003"));
            responseJson.add("msg", new JsonPrimitive("该商品已经添加收藏，请到我的收藏查看"));
            return responseJson.toString();
        }

        long timestamp = System.currentTimeMillis();
        int insertRows = userMapper.addCollect(userId, productId, timestamp);
        if (insertRows > 0) {
            responseJson.add("code", new JsonPrimitive("001"));
            responseJson.add("msg", new JsonPrimitive("添加收藏成功"));
            return responseJson.toString();
        }

        responseJson.add("code", new JsonPrimitive("002"));
        responseJson.add("msg", new JsonPrimitive("添加收藏失败"));
        return responseJson.toString();
    }

    /**
     * 获取用户的所有收藏商品信息
     * @param json 前端请求json(用户id)
     * @return 返回收藏商品详细信息
     */
    public String getCollect(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();

        JsonObject responseJson = new JsonObject();
        List<Collect> collectList = userMapper.getCollectByUserId(userId);
        if (collectList.isEmpty()) {
            responseJson.add("code", new JsonPrimitive("002"));
            responseJson.add("msg", new JsonPrimitive("该用户没有收藏的商品"));
            return responseJson.toString();
        }

        List<Product> productList = new ArrayList<>();
        for (Collect collect : collectList) {
            Product product = productMapper.getProductById(collect.getProduct_id());
            productList.add(product);
        }
        responseJson.add("code", new JsonPrimitive("1"));
        responseJson.add("collectList", new Gson().toJsonTree(productList).getAsJsonArray());
        return responseJson.toString();
    }

    /**
     * 删除用户的收藏商品信息
     * @param json 前端请求json(用户id,商品id)
     * @return 返回收藏删除状态
     */
    public String deleteCollect(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        int userId = jsonObject.get("user_id").getAsInt();
        int productId = jsonObject.get("product_id").getAsInt();

        Collect collect = userMapper.getCollectByUserIdAndProductId(userId, productId);

        JsonObject responseJson = new JsonObject();
        if (collect == null) {
            responseJson.add("code", new JsonPrimitive("002"));
            responseJson.add("msg", new JsonPrimitive("该商品不在收藏列表"));
            return responseJson.toString();
        }

        int deleteRows = userMapper.deleteCollect(userId, productId);
        if (deleteRows > 0) {
            responseJson.add("code", new JsonPrimitive("001"));
            responseJson.add("msg", new JsonPrimitive("删除收藏成功"));
            return responseJson.toString();
        }
        responseJson.add("code", new JsonPrimitive("003"));
        responseJson.add("msg", new JsonPrimitive("删除收藏失败，未知错误"));
        return responseJson.toString();
    }
}
