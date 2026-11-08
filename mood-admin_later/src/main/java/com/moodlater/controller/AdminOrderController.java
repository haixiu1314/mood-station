package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.DailyOrderVO;
import com.moodlater.entity.DashboardVO;
import com.moodlater.entity.Order;
import com.moodlater.entity.OrderRefund;
import com.moodlater.entity.ProductSalesVO;
import com.moodlater.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderController {

    @Autowired
    private AdminOrderService orderService;

    @GetMapping("/list")
    public Result<List<Order>> list() {
        return Result.success(orderService.listAllOrders());
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    /**
     * 发货（含快递信息）
     */
    @PutMapping("/ship/{id}")
    public Result<Void> ship(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String expressCompany = params.get("expressCompany") != null ? params.get("expressCompany").toString() : "";
        String expressNo = params.get("expressNo") != null ? params.get("expressNo").toString() : "";
        String senderName = params.get("senderName") != null ? params.get("senderName").toString() : "";
        String senderAddress = params.get("senderAddress") != null ? params.get("senderAddress").toString() : "";
        String senderPhone = params.get("senderPhone") != null ? params.get("senderPhone").toString() : "";
        orderService.shipOrder(id, expressCompany, expressNo, senderName, senderAddress, senderPhone);
        return Result.success();
    }

    /**
     * 退款审核通过
     */
    @PutMapping("/refund/approve/{id}")
    public Result<Void> approveRefund(@PathVariable Long id, @RequestAttribute("adminId") Long adminId) {
        orderService.refundOrder(id, adminId);
        return Result.success();
    }

    /**
     * 退款审核不通过
     */
    @PutMapping("/refund/reject/{id}")
    public Result<Void> rejectRefund(@PathVariable Long id, @RequestAttribute("adminId") Long adminId) {
        orderService.rejectRefund(id, adminId);
        return Result.success();
    }

    /**
     * 获取待处理退款申请列表
     */
    @GetMapping("/refund/pending")
    public Result<List<OrderRefund>> pendingRefunds() {
        return Result.success(orderService.listPendingRefunds());
    }

    /**
     * 获取待处理退款数量
     */
    @GetMapping("/refund/pending/count")
    public Result<Integer> countPendingRefunds() {
        return Result.success(orderService.countPendingRefunds());
    }

    /**
     * 更新快递状态
     */
    @PutMapping("/express/{id}")
    public Result<Void> updateExpressStatus(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String expressStatus = params.get("expressStatus") != null ? params.get("expressStatus").toString() : "";
        orderService.updateExpressStatus(id, expressStatus);
        return Result.success();
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard(@RequestParam(defaultValue = "7") Integer days) {
        DashboardVO stats = orderService.getDashboardStats(days);
        List<DailyOrderVO> dailyOrders = orderService.getDailyOrders(days);
        List<ProductSalesVO> productSales = orderService.getProductSales(days);
        Map<String, Object> result = new HashMap<>();
        result.put("stats", stats);
        result.put("dailyOrders", dailyOrders);
        result.put("productSales", productSales);
        result.put("pendingRefundCount", orderService.countPendingRefunds());
        return Result.success(result);
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success();
    }
}
