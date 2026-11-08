package com.moodlater.service;

import com.moodlater.entity.ExpressCompany;
import com.moodlater.entity.Order;
import com.moodlater.mapper.AdminOrderMapper;
import com.moodlater.mapper.ExpressCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminExpressService {

    @Autowired
    private AdminOrderMapper orderMapper;

    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;

    /**
     * 获取所有已发货订单
     */
    public List<Order> getShippedOrders() {
        List<Order> orders = orderMapper.selectAllOrders();
        // 过滤出已发货的订单
        return orders.stream()
                .filter(o -> "shipped".equals(o.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * 更新快递状态
     */
    @Transactional
    public void updateExpressStatus(Long orderId, String expressStatus) {
        orderMapper.updateExpressStatus(orderId, expressStatus);
    }

    /**
     * 退款
     */
    @Transactional
    public void refundOrder(Long orderId) {
        orderMapper.refundOrder(orderId);
    }

    /**
     * 获取快递公司列表
     */
    public List<ExpressCompany> getExpressCompanyList() {
        return expressCompanyMapper.selectAll();
    }
}
