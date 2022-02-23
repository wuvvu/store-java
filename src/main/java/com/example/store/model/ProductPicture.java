package com.example.store.model;

public class ProductPicture {
    private int id;
    private int product_id;
    private String product_picture;
    private String intro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(String product_picture) {
        this.product_picture = product_picture;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
