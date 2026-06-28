package com.houserental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houserental.entity.LeaseOrder;
import com.houserental.exception.BusinessException;
import com.houserental.mapper.LeaseOrderMapper;
import com.houserental.service.LeaseOrderService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class LeaseOrderServiceImpl extends ServiceImpl<LeaseOrderMapper, LeaseOrder> implements LeaseOrderService {

    @Override
    public IPage<LeaseOrder> pageOrder(Long pageNum, Long pageSize, Integer status, LocalDate startTime, LocalDate endTime, String keyword) {
        LambdaQueryWrapper<LeaseOrder> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(LeaseOrder::getStatus, status);
        }
        if (startTime != null) {
            wrapper.ge(LeaseOrder::getCreateTime, startTime.atStartOfDay());
        }
        if (endTime != null) {
            wrapper.le(LeaseOrder::getCreateTime, endTime.atTime(23, 59, 59));
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(LeaseOrder::getOrderNo, keyword).or().like(LeaseOrder::getTenantName, keyword));
        }
        wrapper.orderByDesc(LeaseOrder::getCreateTime);
        Page<LeaseOrder> page = new Page<>(pageNum, pageSize);
        return page(page, wrapper);
    }

    @Override
    public LeaseOrder getOrderDetail(Long id) {
        LeaseOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }

    @Override
    public void addOrder(LeaseOrder leaseOrder) {
        leaseOrder.setOrderNo(generateOrderNo());
        if (leaseOrder.getStatus() == null) {
            leaseOrder.setStatus(0);
        }
        save(leaseOrder);
    }

    @Override
    public void updateOrder(LeaseOrder leaseOrder) {
        if (leaseOrder.getId() == null) {
            throw new BusinessException("订单ID不能为空");
        }
        LeaseOrder existOrder = getById(leaseOrder.getId());
        if (existOrder == null) {
            throw new BusinessException("订单不存在");
        }
        leaseOrder.setOrderNo(null);
        updateById(leaseOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        LeaseOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        removeById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        LeaseOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }
        if (status < 0 || status > 3) {
            throw new BusinessException("状态值不正确");
        }
        order.setStatus(status);
        updateById(order);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        return "ORD" + timestamp + String.format("%04d", randomNum);
    }

}
