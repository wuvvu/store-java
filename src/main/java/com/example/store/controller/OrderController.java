package com.example.store.controller;

import com.example.store.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/user/order/getOrder")
    public String getOrder(@RequestBody String json) {
        return orderService.getOrder(json);
    }

    @RequestMapping("/user/order/addOrder")
    public String addOrder(@RequestBody String json) {
        return orderService.addOrder(json);
    }

    @RequestMapping("/user/shoppingCart/getShoppingCart")
    public String getShoppingCart(@RequestBody String json) {
        return orderService.getShoppingCart(json);
    }

    @RequestMapping("/user/shoppingCart/addShoppingCart")
    public String addShoppingCart(@RequestBody String json) {
        return orderService.addShoppingCart(json);
    }

    @RequestMapping("/user/shoppingCart/deleteShoppingCart")
    public String deleteShoppingCart(@RequestBody String json) {
        return orderService.deleteShoppingCart(json);
    }

    @RequestMapping("/user/shoppingCart/updateShoppingCart")
    public String updateShoppingCart(@RequestBody String json) {
        return orderService.updateShoppingCart(json);
    }



}
