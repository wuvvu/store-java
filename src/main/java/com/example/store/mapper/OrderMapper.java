package com.example.store.mapper;

import com.example.store.model.Order;
import com.example.store.model.ShoppingCart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    List<Order> getOrderIdByUserId(@Param("user_id") int userId);

    List<Order> getOrderByUserId(@Param("user_id") int userId);

    int addOrder(List<Order> orderList);

    List<ShoppingCart> getShoppingCartByUserId(@Param("user_id") int userId);

    ShoppingCart getShoppingCartByUserIdAndProductId(@Param("user_id") int userId, @Param("product_id") int productId);

    int addShoppingCart(@Param("user_id") int userId, @Param("product_id") int productId);

    int updateShoppingCart(@Param("newNum") int newNum, @Param("user_id") int userId, @Param("product_id") int productId);

    int deleteShoppingCart(@Param("user_id") int userId, @Param("product_id") int productId);

    int reduceProductByProductId(@Param("product_id") int productId, @Param("num") int num);

    int increaseSalesByProductId(@Param("product_id") int productId, @Param("num") int num);
}
