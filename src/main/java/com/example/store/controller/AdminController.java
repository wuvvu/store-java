package com.example.store.controller;

import com.example.store.service.AdminService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/admin/product/addProduct")
    public String addProduct(@RequestBody String json) {
        return adminService.addProduct(json);
    }

    @RequestMapping("/admin/product/offShelf")
    public String offShelf(@RequestBody String json) {
        return adminService.offShelf(json);
    }

    @RequestMapping("/admin/product/updateProduct")
    public String updateProduct(@RequestBody String json) {
        return adminService.updateProduct(json);
    }

    @RequestMapping("/admin/product/onShelf")
    public String onShelf(@RequestBody String json) {
        return adminService.onShelf(json);
    }

}
