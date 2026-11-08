package com.moodlater.mapper;

import com.moodlater.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> selectAll();

    Product selectById(@Param("id") Long id);

    int insert(Product product);

    int updateById(Product product);

    int deleteById(@Param("id") Long id);

    int decrementStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    int incrementStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
