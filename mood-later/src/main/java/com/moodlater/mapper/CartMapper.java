package com.moodlater.mapper;

import com.moodlater.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CartMapper {

    int insert(Cart cart);

    List<Cart> selectByUserId(@Param("userId") Long userId);

    Cart selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    int updateQuantity(@Param("userId") Long userId, @Param("productId") Long productId, @Param("quantity") Integer quantity);

    int deleteById(@Param("id") Long id);

    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    int clearByUserId(@Param("userId") Long userId);

    int updateSelectedSpec(@Param("userId") Long userId, @Param("productId") Long productId, @Param("selectedSpec") String selectedSpec);
}
