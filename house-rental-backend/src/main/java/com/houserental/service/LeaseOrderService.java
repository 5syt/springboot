package com.houserental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.houserental.entity.LeaseOrder;

import java.time.LocalDate;

public interface LeaseOrderService extends IService<LeaseOrder> {

    IPage<LeaseOrder> pageOrder(Long pageNum, Long pageSize, Integer status, LocalDate startTime, LocalDate endTime, String keyword);

    LeaseOrder getOrderDetail(Long id);

    void addOrder(LeaseOrder leaseOrder);

    void updateOrder(LeaseOrder leaseOrder);

    void deleteOrder(Long id);

    void updateStatus(Long id, Integer status);

}
