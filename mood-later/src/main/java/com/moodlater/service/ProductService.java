package com.moodlater.service;

import com.moodlater.entity.Product;
import com.moodlater.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<Product> getAllProducts() {
        return productMapper.selectAll();
    }

    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }
}
