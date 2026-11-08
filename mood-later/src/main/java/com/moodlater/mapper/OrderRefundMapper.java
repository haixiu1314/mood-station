package com.moodlater.mapper;

import com.moodlater.entity.OrderRefund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderRefundMapper {

    int insert(OrderRefund refund);

    OrderRefund selectById(@Param("id") Long id);

    List<OrderRefund> selectByOrderId(@Param("orderId") Long orderId);

    List<OrderRefund> selectByUserId(@Param("userId") Long userId);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("reviewerId") Long reviewerId);

    int countPendingByUserId(@Param("userId") Long userId);
}
