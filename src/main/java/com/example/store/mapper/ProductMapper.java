package com.example.store.mapper;

import com.example.store.model.Category;
import com.example.store.model.Product;
import com.example.store.model.ProductPicture;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {

    List<Product> getAllProduct();

    List<Category> getCategory();

    Category getCategoryByName(@Param("categoryName") String categoryName);

    Product getProductById(@Param("id") int id);

    List<ProductPicture> getDetailsPictureByProductId(@Param("productId") int productId);

    List<Product> getPromoProductByCategoryId(@Param("categoryId") int id);

    List<Product> getProductByCategoryId(@Param("categoryId") int id);

    List<Product> getProductBySearch(@Param("search") String search);
}
