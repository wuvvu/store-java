package com.example.store.controller;

import com.example.store.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录
     * @param json 账号密码json
     * @return 登录信息
     */
    @RequestMapping("/users/login")
    public String login(@RequestBody String json, HttpSession session) {
        return userService.login(json, session);
    }

    /**
     * 查询是否存在某个用户名,用于注册时前端校验
     * @param json 账号密码json
     * @return 用户信息
     */
    @RequestMapping("/users/findUserName")
    public String findUserName(@RequestBody String json) {
        return userService.findUserName(json);
    }

    /**
     * 注册
     * @param json 账号密码json
     * @return 登录信息
     */
    @RequestMapping("/users/register")
    public String register(@RequestBody String json) {
        return userService.register(json);
    }

    @RequestMapping("/user/collect/addCollect")
    public String addCollect(@RequestBody String json) {
        return userService.addCollect(json);
    }

    @RequestMapping("/user/collect/getCollect")
    public String getCollect(@RequestBody String json) {
        return userService.getCollect(json);
    }

    @RequestMapping("/user/collect/deleteCollect")
    public String deleteCollect(@RequestBody String json) {
        return userService.deleteCollect(json);
    }



}
