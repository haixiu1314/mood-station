package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Product;
import com.moodlater.service.AdminProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {

    @Autowired
    private AdminProductService productService;

    @GetMapping("/list")
    public Result<List<Product>> list() {
        return Result.success(productService.listAllProducts());
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Product product) {
        productService.addProduct(product);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Product product) {
        productService.updateProduct(product);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        productService.updateProductStatus(id, params.get("status"));
        return Result.success();
    }

    @PostMapping("/restock")
    public Result<Void> restock(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long productId = Long.valueOf(params.get("productId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        Long adminId = (Long) request.getAttribute("adminId");
        productService.restockProduct(productId, quantity, adminId);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    @PostMapping("/uploadCover")
    public Result<String> uploadCover(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        String imageUrl = productService.uploadProductImage(file);
        return Result.success(imageUrl);
    }

    @PostMapping("/uploadImages")
    public Result<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return Result.error("文件不能为空");
        }
        if (files.length > 4) {
            return Result.error("最多上传4张图片");
        }
        List<String> imageUrls = productService.uploadProductImages(files);
        return Result.success(imageUrls);
    }
}
