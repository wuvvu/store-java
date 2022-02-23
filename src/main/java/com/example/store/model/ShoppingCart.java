package com.example.store.model;

import java.math.BigDecimal;

public class ShoppingCart {

    private int id;
    private int user_id;
    private int product_id;
    private int num;

    private String productName;
    private String productImg;
    private BigDecimal product_price;
    private int product_num;
    private boolean check;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
