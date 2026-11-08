package com.moodlater.service;

import com.moodlater.entity.Cart;
import com.moodlater.entity.CartVO;
import com.moodlater.entity.Product;
import com.moodlater.mapper.CartMapper;
import com.moodlater.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public void addToCart(Long userId, Long productId, Integer quantity, String selectedSpec) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("数量必须大于0");
        }

        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        Cart exist = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (exist != null) {
            cartMapper.updateQuantity(userId, productId, exist.getQuantity() + quantity);
            // 更新规格（如果传入了新规格）
            if (selectedSpec != null && !selectedSpec.isEmpty()) {
                cartMapper.updateSelectedSpec(userId, productId, selectedSpec);
            }
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setSelectedSpec(selectedSpec);
            cart.setQuantity(quantity);
            cartMapper.insert(cart);
        }
    }

    public List<CartVO> getCartList(Long userId) {
        List<Cart> carts = cartMapper.selectByUserId(userId);
        List<CartVO> result = new ArrayList<>();
        for (Cart cart : carts) {
            Product product = productMapper.selectById(cart.getProductId());
            if (product != null) {
                CartVO vo = new CartVO();
                vo.setId(cart.getId());
                vo.setProductId(product.getId());
                vo.setProductName(product.getName());
                vo.setProductImage(product.getCoverImage());
                vo.setSelectedSpec(cart.getSelectedSpec());
                vo.setPrice(product.getPrice().intValue());
                vo.setQuantity(cart.getQuantity());
                vo.setStock(product.getStock());
                result.add(vo);
            }
        }
        return result;
    }

    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            cartMapper.deleteByUserIdAndProductId(userId, productId);
        } else {
            cartMapper.updateQuantity(userId, productId, quantity);
        }
    }

    public void removeFromCart(Long userId, Long productId) {
        cartMapper.deleteByUserIdAndProductId(userId, productId);
    }

    public void clearCart(Long userId) {
        cartMapper.clearByUserId(userId);
    }
}
