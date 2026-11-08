package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.ExpressCompany;
import com.moodlater.entity.Order;
import com.moodlater.service.AdminExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/express")
public class AdminExpressController {

    @Autowired
    private AdminExpressService expressService;

    /**
     * 获取所有已发货订单（用于快递状态管理）
     */
    @GetMapping("/shipped")
    public Result<List<Order>> getShippedOrders() {
        return Result.success(expressService.getShippedOrders());
    }

    /**
     * 更新快递状态
     */
    @PutMapping("/status/{id}")
    public Result<Void> updateExpressStatus(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String expressStatus = params.get("expressStatus") != null ? params.get("expressStatus").toString() : "";
        expressService.updateExpressStatus(id, expressStatus);
        return Result.success();
    }

    /**
     * 退款（快递状态管理页面直接退款）
     */
    @PutMapping("/refund/{id}")
    public Result<Void> refundOrder(@PathVariable Long id) {
        expressService.refundOrder(id);
        return Result.success();
    }

    /**
     * 获取快递公司列表
     */
    @GetMapping("/companies")
    public Result<List<ExpressCompany>> getExpressCompanies() {
        return Result.success(expressService.getExpressCompanyList());
    }
}
