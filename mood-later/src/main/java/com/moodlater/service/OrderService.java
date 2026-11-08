package com.moodlater.service;

import com.moodlater.entity.*;
import com.moodlater.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private DailyRewardService dailyRewardService;

    @Transactional
    public OrderVO createOrder(Long userId, List<Long> cartIds, Long productId, Integer quantity, Long addressId, String selectedSpec) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        int totalAmount = 0;
        List<OrderItem> items = new ArrayList<>();
        List<Cart> selectedCarts = new ArrayList<>();

        if (productId != null) {
            Product product = productMapper.selectById(productId);
            if (product == null) {
                throw new RuntimeException("商品不存在");
            }
            if (product.getStock() < quantity) {
                throw new RuntimeException("商品库存不足: " + product.getName());
            }

            int price = product.getPrice().setScale(0, java.math.RoundingMode.HALF_UP).intValue();
            int subtotal = price * quantity;
            totalAmount += subtotal;

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductImage(product.getCoverImage());
            item.setSelectedSpec(selectedSpec);
            item.setPrice(price);
            item.setQuantity(quantity);
            items.add(item);
        } else {
            List<Cart> carts = cartMapper.selectByUserId(userId);
            if (carts.isEmpty()) {
                throw new RuntimeException("购物车为空，请先添加商品");
            }

            if (cartIds == null || cartIds.isEmpty()) {
                selectedCarts.addAll(carts);
            } else {
                for (Cart cart : carts) {
                    for (Object cartId : cartIds) {
                        if (cart.getId().equals(Long.valueOf(cartId.toString()))) {
                            selectedCarts.add(cart);
                            break;
                        }
                    }
                }
            }

            if (selectedCarts.isEmpty()) {
                throw new RuntimeException("未找到指定的购物车商品，请检查cartIds是否正确");
            }

            for (Cart cart : selectedCarts) {
                Product product = productMapper.selectById(cart.getProductId());
                if (product == null) {
                    throw new RuntimeException("商品不存在: " + cart.getProductId());
                }
                if (product.getStock() < cart.getQuantity()) {
                    throw new RuntimeException("商品库存不足: " + product.getName());
                }

                int price = product.getPrice().setScale(0, java.math.RoundingMode.HALF_UP).intValue();
                int subtotal = price * cart.getQuantity();
                totalAmount += subtotal;

                OrderItem item = new OrderItem();
                item.setProductId(product.getId());
                item.setProductName(product.getName());
                item.setProductImage(product.getCoverImage());
                item.setSelectedSpec(cart.getSelectedSpec());
                item.setPrice(price);
                item.setQuantity(cart.getQuantity());
                items.add(item);
            }
        }

        if (user.getCoinBalance() < totalAmount) {
            throw new RuntimeException("心动币余额不足");
        }

        Address address = null;
        if (addressId != null) {
            address = addressMapper.selectById(addressId);
            if (address == null || !address.getUserId().equals(userId)) {
                throw new RuntimeException("收货地址不存在或无权使用");
            }
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        
        if (address != null) {
            order.setAddressName(address.getName());
            order.setAddressPhone(address.getPhone());
            order.setAddressDetail(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetail());
        } else {
            order.setAddressName(user.getNickname());
            order.setAddressPhone("");
            order.setAddressDetail("");
        }
        
        orderMapper.insert(order);

        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderMapper.insertItem(item);
            productMapper.decrementStock(item.getProductId(), item.getQuantity());
        }

        for (Cart cart : selectedCarts) {
            cartMapper.deleteById(cart.getId());
        }

        user.setCoinBalance(user.getCoinBalance() - totalAmount);
        userMapper.updateById(user);

        return getOrderDetail(order.getId());
    }

    public List<OrderVO> getUserOrders(Long userId) {
        List<Order> orders = orderMapper.selectByUserId(userId);
        List<OrderVO> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(buildOrderVO(order, userId));
        }
        return result;
    }

    public List<OrderVO> getUserOrdersByStatus(Long userId, String status) {
        List<Order> orders = orderMapper.selectByUserIdAndStatus(userId, status);
        List<OrderVO> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(buildOrderVO(order, userId));
        }
        return result;
    }

    public OrderVO getOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return null;
        }
        return buildOrderVO(order, order.getUserId());
    }

    /**
     * 用户取消订单（待发货状态）
     */
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }
        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("只有待发货订单可以取消");
        }

        // 恢复库存
        List<OrderItem> items = orderMapper.selectItemsByOrderId(orderId);
        for (OrderItem item : items) {
            productMapper.incrementStock(item.getProductId(), item.getQuantity());
        }

        // 退还心动币
        User user = userMapper.selectById(userId);
        user.setCoinBalance(user.getCoinBalance() + order.getTotalAmount());
        userMapper.updateById(user);

        // 更新订单状态为已取消
        orderMapper.cancelOrder(orderId);
    }

    /**
     * 用户申请退款（已发货状态）
     */
    @Transactional
    public void applyRefund(Long userId, Long orderId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }
        if (!"shipped".equals(order.getStatus()) && !"pending".equals(order.getStatus())) {
            throw new RuntimeException("只有待发货或已发货订单可以申请退款");
        }

        // 检查是否已有待审核的退款申请
        List<OrderRefund> existing = orderRefundMapper.selectByOrderId(orderId);
        for (OrderRefund refund : existing) {
            if (refund.getStatus() == 0) {
                throw new RuntimeException("该订单已有待审核的退款申请");
            }
        }

        OrderRefund refund = new OrderRefund();
        refund.setOrderId(orderId);
        refund.setUserId(userId);
        refund.setOrderNo(order.getOrderNo());
        refund.setTotalAmount(order.getTotalAmount());
        refund.setReason(reason);
        orderRefundMapper.insert(refund);
    }

    /**
     * 用户确认收货
     */
    @Transactional
    public Map<String, Object> confirmReceived(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }
        if ("completed".equals(order.getStatus())) {
            throw new RuntimeException("订单已完成，无需重复操作");
        }
        if (!"shipped".equals(order.getStatus()) && !"signed".equals(order.getStatus())) {
            throw new RuntimeException("只有已发货或已签收订单可以确认收货");
        }

        orderMapper.confirmReceived(orderId);

        // 确认收货奖励（5个心动币，无每日限制）
        Map<String, Object> result = new HashMap<>();
        int rewardAmount = 5;
        userMapper.addBalance(userId, rewardAmount);
        result.put("rewardMessage", "确认收货成功，获得" + rewardAmount + "个心动币");
        result.put("rewardAmount", rewardAmount);

        return result;
    }

    private OrderVO buildOrderVO(Order order, Long userId) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setStatusText(getStatusText(order.getStatus(), order.getExpressStatus()));
        vo.setAddressName(order.getAddressName());
        vo.setAddressPhone(order.getAddressPhone());
        vo.setAddressDetail(order.getAddressDetail());
        vo.setCreateTime(order.getCreateTime());
        vo.setShippedTime(order.getShippedTime());
        vo.setRefundedTime(order.getRefundedTime());
        vo.setExpressCompany(order.getExpressCompany());
        vo.setExpressNo(order.getExpressNo());
        vo.setExpressStatus(order.getExpressStatus());
        vo.setReceivedTime(order.getReceivedTime());
        vo.setConfirmedTime(order.getConfirmedTime());
        // 设置发货人信息
        vo.setSenderName(order.getSenderName());
        vo.setSenderAddress(order.getSenderAddress());
        vo.setSenderPhone(order.getSenderPhone());
        vo.setItems(orderMapper.selectItemsByOrderId(order.getId()));

        // 查询退款信息
        List<OrderRefund> refunds = orderRefundMapper.selectByOrderId(order.getId());
        if (!refunds.isEmpty()) {
            OrderRefund latest = refunds.get(0);
            vo.setRefundReason(latest.getReason());
            vo.setRefundStatus(latest.getStatus());
        }

        return vo;
    }

    private String getStatusText(String status, String expressStatus) {
        switch (status) {
            case "pending": return "待发货";
            case "shipped":
                if ("signed".equals(expressStatus)) {
                    return "已签收";
                }
                return "派送中";
            case "completed": return "已完成";
            case "cancelled": return "已取消";
            case "refunded": return "已退款";
            default: return "未知";
        }
    }

    private String generateOrderNo() {
        return System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
}
