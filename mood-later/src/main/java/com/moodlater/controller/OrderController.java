package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.OrderVO;
import com.moodlater.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<OrderVO> createOrder(@RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        @SuppressWarnings("unchecked")
        List<Long> cartIds = (List<Long>) params.get("cartIds");
        Long productId = params.get("productId") != null ? Long.valueOf(params.get("productId").toString()) : null;
        Integer quantity = params.get("quantity") != null ? Integer.valueOf(params.get("quantity").toString()) : null;
        Long addressId = params.get("addressId") != null ? Long.valueOf(params.get("addressId").toString()) : null;
        String selectedSpec = params.get("selectedSpec") != null ? params.get("selectedSpec").toString() : null;
        OrderVO order = orderService.createOrder(userId, cartIds, productId, quantity, addressId, selectedSpec);
        return Result.success(order);
    }

    @GetMapping("/list")
    public Result<List<OrderVO>> getOrderList(@RequestAttribute("userId") Long userId) {
        List<OrderVO> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }

    @GetMapping("/list/{status}")
    public Result<List<OrderVO>> getOrdersByStatus(@RequestAttribute("userId") Long userId, @PathVariable String status) {
        List<OrderVO> orders = orderService.getUserOrdersByStatus(userId, status);
        return Result.success(orders);
    }

    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        OrderVO order = orderService.getOrderDetail(id);
        return Result.success(order);
    }

    /**
     * 取消订单（待发货状态）
     */
    @PostMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        orderService.cancelOrder(userId, id);
        return Result.success();
    }

    /**
     * 申请退款
     */
    @PostMapping("/refund/{id}")
    public Result<Void> applyRefund(@PathVariable Long id, @RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        String reason = params.get("reason") != null ? params.get("reason").toString() : "";
        if (reason.trim().isEmpty()) {
            return Result.error("退款原因不能为空");
        }
        orderService.applyRefund(userId, id, reason);
        return Result.success();
    }

    /**
     * 确认收货
     */
    @PostMapping("/confirm/{id}")
    public Result<Map<String, Object>> confirmReceived(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Map<String, Object> result = orderService.confirmReceived(userId, id);
        return Result.success(result);
    }
}
