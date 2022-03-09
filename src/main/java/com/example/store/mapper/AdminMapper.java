package com.example.store.mapper;

import com.example.store.model.Order;
import com.example.store.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AdminMapper {

    int addProduct(Product product);

    int offShelfById(@Param("product_id") int productId);

    int updateProduct(Product product);

    int onShelfById(@Param("product_id") int productId);

    int addProductPicture(@Param("product_id") int productId, @Param("pictureList") List<String> pictureList);

    int deletePictureByProductId(@Param("product_id") int productId);

    List<Product> getAllProduct();

    List<Order> getOrderIdBySearch(@Param("search") String search);

    List<Order> getOrderBySearch(@Param("search") String search);

    List<Map<String, Object>> getCategorySales();
}
