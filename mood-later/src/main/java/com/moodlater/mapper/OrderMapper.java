package com.moodlater.mapper;

import com.moodlater.entity.Order;
import com.moodlater.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper {

    int insert(Order order);

    int insertItem(OrderItem item);

    Order selectById(@Param("id") Long id);

    Order selectByOrderNo(@Param("orderNo") String orderNo);

    List<Order> selectByUserId(@Param("userId") Long userId);

    List<Order> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    List<OrderItem> selectItemsByOrderId(@Param("orderId") Long orderId);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int updateShippedTime(@Param("id") Long id);

    int updateRefundedTime(@Param("id") Long id);

    // 取消订单
    int cancelOrder(@Param("id") Long id);

    // 发货（含快递信息）
    int shipOrderWithExpress(@Param("id") Long id, @Param("expressCompany") String expressCompany,
                             @Param("expressNo") String expressNo, @Param("expressCourier") String expressCourier);

    // 确认收货
    int confirmReceived(@Param("id") Long id);

    // 快递状态更新
    int updateExpressStatus(@Param("id") Long id, @Param("expressStatus") String expressStatus);
}
