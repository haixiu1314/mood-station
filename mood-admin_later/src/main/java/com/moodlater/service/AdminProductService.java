package com.moodlater.service;

import com.moodlater.entity.Product;
import com.moodlater.entity.StockLog;
import com.moodlater.mapper.AdminProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminProductService {

    @Autowired
    private AdminProductMapper productMapper;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    public List<Product> listAllProducts() {
        return productMapper.selectAllProducts();
    }

    public Product getProductById(Long id) {
        return productMapper.selectProductById(id);
    }

    @Transactional
    public void addProduct(Product product) {
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        if (product.getStock() == null) {
            product.setStock(0);
        }
        if (product.getSortOrder() == null) {
            product.setSortOrder(0);
        }
        productMapper.insertProduct(product);
    }

    @Transactional
    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    @Transactional
    public void updateProductStatus(Long id, Integer status) {
        productMapper.updateProductStatus(id, status);
    }

    @Transactional
    public void restockProduct(Long productId, Integer quantity, Long operatorId) {
        productMapper.restockProduct(productId, quantity);
        Product product = productMapper.selectProductById(productId);
        StockLog stockLog = new StockLog();
        stockLog.setProductId(productId);
        stockLog.setChangeAmount(quantity);
        stockLog.setStockAfter(product.getStock());
        stockLog.setOperatorId(operatorId);
        stockLog.setReason("管理端补货");
        productMapper.insertStockLog(stockLog);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }

    public String uploadProductImage(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(uploadPath, "product");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadDir.resolve(fileName);
            file.transferTo(filePath.toFile());
            return "http://localhost:8081/uploads/product/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }

    public List<String> uploadProductImages(MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadProductImage(file));
        }
        return urls;
    }
}
