package com.houserental.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.houserental.common.Result;
import com.houserental.entity.House;
import com.houserental.entity.HouseType;
import com.houserental.entity.LeaseOrder;
import com.houserental.service.HouseService;
import com.houserental.service.HouseTypeService;
import com.houserental.service.LeaseOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Resource
    private HouseService houseService;

    @Resource
    private LeaseOrderService leaseOrderService;

    @Resource
    private HouseTypeService houseTypeService;

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        Map<String, Object> data = new HashMap<>();
        long houseTotal = houseService.count();
        long houseRenting = houseService.count(new LambdaQueryWrapper<House>().eq(House::getStatus, 1));
        long orderTotal = leaseOrderService.count();
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        long monthNewOrders = leaseOrderService.count(
                new LambdaQueryWrapper<LeaseOrder>()
                        .ge(LeaseOrder::getCreateTime, firstDayOfMonth.atStartOfDay())
        );
        data.put("houseTotal", houseTotal);
        data.put("houseRenting", houseRenting);
        data.put("orderTotal", orderTotal);
        data.put("monthNewOrders", monthNewOrders);
        return Result.success(data);
    }

    @GetMapping("/houseTypePie")
    public Result<List<Map<String, Object>>> houseTypePie() {
        List<HouseType> typeList = houseTypeService.list();
        List<Map<String, Object>> result = new ArrayList<>();
        for (HouseType type : typeList) {
            long count = houseService.count(new LambdaQueryWrapper<House>().eq(House::getTypeId, type.getId()));
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getName());
            item.put("value", count);
            result.add(item);
        }
        return Result.success(result);
    }

    @GetMapping("/orderTrend")
    public Result<Map<String, Object>> orderTrend() {
        List<String> months = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 5; i >= 0; i--) {
            LocalDate date = now.minusMonths(i);
            String monthStr = date.format(formatter);
            months.add(monthStr);
            LocalDate firstDay = date.withDayOfMonth(1);
            LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());
            LocalDateTime startTime = firstDay.atStartOfDay();
            LocalDateTime endTime = lastDay.atTime(23, 59, 59);
            long count = leaseOrderService.count(
                    new LambdaQueryWrapper<LeaseOrder>()
                            .ge(LeaseOrder::getCreateTime, startTime)
                            .le(LeaseOrder::getCreateTime, endTime)
            );
            counts.add(count);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("months", months);
        data.put("counts", counts);
        return Result.success(data);
    }

}
