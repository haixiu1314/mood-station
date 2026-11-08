package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.CartVO;
import com.moodlater.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Result<Void> addToCart(@RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        Long productId = Long.valueOf(params.get("productId").toString());
        Integer quantity = params.get("quantity") != null ? Integer.valueOf(params.get("quantity").toString()) : 1;
        String selectedSpec = params.get("selectedSpec") != null ? params.get("selectedSpec").toString() : null;
        cartService.addToCart(userId, productId, quantity, selectedSpec);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<CartVO>> getCartList(@RequestAttribute("userId") Long userId) {
        List<CartVO> list = cartService.getCartList(userId);
        return Result.success(list);
    }

    @PutMapping("/update")
    public Result<Void> updateQuantity(@RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        Long productId = Long.valueOf(params.get("productId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        cartService.updateQuantity(userId, productId, quantity);
        return Result.success();
    }

    @DeleteMapping("/remove/{productId}")
    public Result<Void> removeFromCart(@PathVariable Long productId, @RequestAttribute("userId") Long userId) {
        cartService.removeFromCart(userId, productId);
        return Result.success();
    }

    @DeleteMapping("/clear")
    public Result<Void> clearCart(@RequestAttribute("userId") Long userId) {
        cartService.clearCart(userId);
        return Result.success();
    }
}
