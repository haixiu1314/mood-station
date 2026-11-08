package com.moodlater.mapper;

import com.moodlater.entity.DailyOrderVO;
import com.moodlater.entity.DashboardVO;
import com.moodlater.entity.Order;
import com.moodlater.entity.OrderItem;
import com.moodlater.entity.OrderRefund;
import com.moodlater.entity.ProductSalesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminOrderMapper {
    List<Order> selectAllOrders();
    Order selectOrderById(@Param("id") Long id);
    List<OrderItem> selectOrderItems(@Param("orderId") Long orderId);
    int shipOrder(@Param("id") Long id);
    int refundOrder(@Param("id") Long id);
    int incrementProductStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    DashboardVO selectDashboardStats(@Param("days") Integer days);
    List<DailyOrderVO> selectDailyOrders(@Param("days") Integer days);
    List<ProductSalesVO> selectProductSales(@Param("days") Integer days);

    // 发货（含快递信息）
    int shipOrderWithExpress(@Param("id") Long id, @Param("expressCompany") String expressCompany,
                             @Param("expressNo") String expressNo,
                             @Param("senderName") String senderName, @Param("senderAddress") String senderAddress,
                             @Param("senderPhone") String senderPhone);

    // 取消订单
    int cancelOrder(@Param("id") Long id);

    // 确认收货（成交统计用）
    int confirmReceived(@Param("id") Long id);

    // 快递状态更新
    int updateExpressStatus(@Param("id") Long id, @Param("expressStatus") String expressStatus);

    // 退款申请管理
    List<OrderRefund> selectAllPendingRefunds();
    OrderRefund selectRefundById(@Param("id") Long id);
    int updateRefundStatus(@Param("id") Long id, @Param("status") Integer status, @Param("reviewerId") Long reviewerId);
    int countPendingRefunds();

    // 删除订单
    int deleteOrderItems(@Param("id") Long id);
    int deleteOrder(@Param("id") Long id);
}
