package com.example.store.mapper;

import com.example.store.model.Collect;
import com.example.store.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    User getUserByUsername(@Param("username") String username);

    int register(@Param("username") String username, @Param("password") String password);

    int addCollect(@Param("user_id") int userId, @Param("product_id") int productId, @Param("timestamp") long timestamp);

    List<Collect> getCollectByUserId(@Param("user_id") int userId);

    Collect getCollectByUserIdAndProductId(@Param("user_id") int userId, @Param("product_id") int productId);

    int deleteCollect(@Param("user_id") int userId, @Param("product_id") int productId);

}
