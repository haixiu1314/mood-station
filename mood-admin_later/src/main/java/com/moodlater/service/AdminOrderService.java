package com.moodlater.service;

import com.moodlater.entity.*;
import com.moodlater.mapper.AdminOrderMapper;
import com.moodlater.mapper.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminOrderService {

    @Autowired
    private AdminOrderMapper orderMapper;

    @Autowired
    private AdminUserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    public List<Order> listAllOrders() {
        List<Order> orders = orderMapper.selectAllOrders();
        for (Order order : orders) {
            order.setItems(orderMapper.selectOrderItems(order.getId()));
        }
        return orders;
    }

    public Map<String, Object> getOrderDetail(Long id) {
        Order order = orderMapper.selectOrderById(id);
        List<OrderItem> items = orderMapper.selectOrderItems(id);
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return result;
    }

    /**
     * 发货（含快递信息）
     */
    @Transactional
    public void shipOrder(Long id, String expressCompany, String expressNo,
                          String senderName, String senderAddress, String senderPhone) {
        Order order = orderMapper.selectOrderById(id);
        orderMapper.shipOrderWithExpress(id, expressCompany, expressNo, senderName, senderAddress, senderPhone);
        // 发送通知
        notificationService.sendNotification(order.getUserId(), "system", "order_ship", "订单发货",
                "您的订单发货成功，慢慢等待美好送达哦｡:.ﾟヽ(｡◕‿◕｡)ﾉﾟ.:｡+ﾟ", order.getId());
    }

    /**
     * 管理员退款审核通过
     */
    @Transactional
    public void refundOrder(Long refundId, Long reviewerId) {
        OrderRefund refund = orderMapper.selectRefundById(refundId);
        if (refund == null) {
            throw new RuntimeException("退款申请不存在");
        }
        if (refund.getStatus() != 0) {
            throw new RuntimeException("该申请已处理");
        }

        Order order = orderMapper.selectOrderById(refund.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 恢复库存
        List<OrderItem> items = orderMapper.selectOrderItems(order.getId());
        for (OrderItem item : items) {
            orderMapper.incrementProductStock(item.getProductId(), item.getQuantity());
        }

        // 退还心动币
        userMapper.updateCoinBalance(order.getUserId(), order.getTotalAmount().intValue());

        User user = userMapper.selectUserById(order.getUserId());
        CoinLog coinLog = new CoinLog();
        coinLog.setUserId(order.getUserId());
        coinLog.setChangeAmount(order.getTotalAmount().intValue());
        coinLog.setBalanceAfter(user.getCoinBalance());
        coinLog.setType("refund");
        coinLog.setReason("订单退款审核通过 - 订单号: " + order.getOrderNo());
        coinLog.setRelatedId(order.getId());
        userMapper.insertCoinLog(coinLog);

        // 更新退款申请状态
        orderMapper.updateRefundStatus(refundId, 1, reviewerId);

        // 更新订单状态
        orderMapper.refundOrder(order.getId());
        
        // 发送通知
        notificationService.sendNotification(order.getUserId(), "system", "refund_success", "退款成功",
                "您的订单退款成功了", order.getId());
    }

    /**
     * 退款审核不通过
     */
    @Transactional
    public void rejectRefund(Long refundId, Long reviewerId) {
        OrderRefund refund = orderMapper.selectRefundById(refundId);
        if (refund == null) {
            throw new RuntimeException("退款申请不存在");
        }
        orderMapper.updateRefundStatus(refundId, 2, reviewerId);
        
        // 发送通知
        Order order = orderMapper.selectOrderById(refund.getOrderId());
        notificationService.sendNotification(order.getUserId(), "system", "refund_fail", "退款失败",
                "您的订单退款失败", order.getId());
    }

    /**
     * 获取待处理退款申请
     */
    public List<OrderRefund> listPendingRefunds() {
        return orderMapper.selectAllPendingRefunds();
    }

    /**
     * 获取待处理退款数量
     */
    public int countPendingRefunds() {
        return orderMapper.countPendingRefunds();
    }

    /**
     * 更新快递状态
     */
    @Transactional
    public void updateExpressStatus(Long orderId, String expressStatus) {
        orderMapper.updateExpressStatus(orderId, expressStatus);
    }

    public DashboardVO getDashboardStats(Integer days) {
        return orderMapper.selectDashboardStats(days);
    }

    public List<DailyOrderVO> getDailyOrders(Integer days) {
        return orderMapper.selectDailyOrders(days);
    }

    public List<ProductSalesVO> getProductSales(Integer days) {
        return orderMapper.selectProductSales(days);
    }

    /**
     * 删除订单
     */
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderMapper.selectOrderById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 如果订单有待处理退款申请，先更新退款状态
        orderMapper.deleteOrderItems(id);
        orderMapper.deleteOrder(id);
    }
}
