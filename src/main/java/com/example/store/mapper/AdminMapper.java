package com.example.store.mapper;

import com.example.store.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {

    int addProduct(Product product);

    int offShelfById(@Param("product_id") int productId);

    int updateProduct(Product product);

    int onShelfById(@Param("product_id") int productId);
}
