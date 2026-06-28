package com.houserental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.houserental.common.Result;
import com.houserental.entity.LeaseOrder;
import com.houserental.service.LeaseOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/order/lease")
public class LeaseOrderController {

    @Resource
    private LeaseOrderService leaseOrderService;

    @GetMapping("/page")
    public Result<IPage<LeaseOrder>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime,
            @RequestParam(required = false) String keyword) {
        IPage<LeaseOrder> page = leaseOrderService.pageOrder(pageNum, pageSize, status, startTime, endTime, keyword);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<LeaseOrder> getById(@PathVariable Long id) {
        LeaseOrder order = leaseOrderService.getOrderDetail(id);
        return Result.success(order);
    }

    @PostMapping
    public Result<Void> add(@RequestBody LeaseOrder leaseOrder) {
        leaseOrderService.addOrder(leaseOrder);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody LeaseOrder leaseOrder) {
        leaseOrderService.updateOrder(leaseOrder);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        leaseOrderService.deleteOrder(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        leaseOrderService.updateStatus(id, status);
        return Result.success();
    }

}
