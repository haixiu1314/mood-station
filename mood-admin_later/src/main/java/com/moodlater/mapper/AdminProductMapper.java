package com.moodlater.mapper;

import com.moodlater.entity.Product;
import com.moodlater.entity.StockLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminProductMapper {
    List<Product> selectAllProducts();
    Product selectProductById(@Param("id") Long id);
    int insertProduct(Product product);
    int updateProduct(Product product);
    int updateProductStatus(@Param("id") Long id, @Param("status") Integer status);
    int restockProduct(@Param("id") Long id, @Param("quantity") Integer quantity);
    int deleteProduct(@Param("id") Long id);

    int insertStockLog(StockLog stockLog);
}
