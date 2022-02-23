package com.example.store.controller;

import com.example.store.service.ProductService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/getAllProduct")
    public String getAllProduct(@RequestBody String json) {
        return productService.getAllProduct(json);
    }

    @RequestMapping("/getPromoProduct")
    public String getPromoProduct(@RequestBody String json) {
        return productService.getPromoProduct(json);
    }

    @RequestMapping("/getHotProduct")
    public String getHotProduct(@RequestBody String json) {
        return productService.getHotProduct(json);
    }

    @RequestMapping("/getProductByCategory")
    public String getProductByCategory(@RequestBody String json) {
        return productService.getProductByCategory(json);
    }

    @RequestMapping("/getCategory")
    public String getCategory() {
        return productService.getCategory();
    }

    @RequestMapping("/getProductBySearch")
    public String getProductBySearch(@RequestBody String json) {
        return productService.getProductBySearch(json);
    }

    @RequestMapping("/getDetails")
    public String getDetails(@RequestBody String json) {
        return productService.getDetails(json);
    }

    @RequestMapping("/getDetailsPicture")
    public String getDetailsPicture(@RequestBody String json) {
        return productService.getDetailsPicture(json);
    }

}
