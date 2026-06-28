package com.houserental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houserental.entity.House;
import com.houserental.entity.LeaseOrder;
import com.houserental.exception.BusinessException;
import com.houserental.mapper.LeaseOrderMapper;
import com.houserental.service.HouseService;
import com.houserental.service.LeaseOrderService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class LeaseOrderServiceImpl extends ServiceImpl<LeaseOrderMapper, LeaseOrder> implements LeaseOrderService {

    @Resource
    private HouseService houseService;

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
        IPage<LeaseOrder> result = page(page, wrapper);
        fillHouseTitle(result.getRecords());
        return result;
    }

    @Override
    public LeaseOrder getOrderDetail(Long id) {
        LeaseOrder order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        fillHouseTitle(java.util.Collections.singletonList(order));
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

    @Override
    public IPage<LeaseOrder> pageMyOrder(Long pageNum, Long pageSize, Integer status, Long userId) {
        LambdaQueryWrapper<LeaseOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeaseOrder::getTenantId, userId);
        if (status != null) {
            wrapper.eq(LeaseOrder::getStatus, status);
        }
        wrapper.orderByDesc(LeaseOrder::getCreateTime);
        Page<LeaseOrder> page = new Page<>(pageNum, pageSize);
        IPage<LeaseOrder> result = page(page, wrapper);
        fillHouseTitle(result.getRecords());
        return result;
    }

    @Override
    public void applyOrder(LeaseOrder leaseOrder) {
        if (leaseOrder.getHouseId() == null) {
            throw new BusinessException("房屋ID不能为空");
        }
        if (leaseOrder.getStartDate() == null) {
            throw new BusinessException("入住日期不能为空");
        }
        if (leaseOrder.getLeaseMonths() == null || leaseOrder.getLeaseMonths() < 1) {
            throw new BusinessException("租期不能为空");
        }
        House house = houseService.getById(leaseOrder.getHouseId());
        if (house == null) {
            throw new BusinessException("房屋不存在");
        }
        if (house.getStatus() == null || house.getStatus() != 1) {
            throw new BusinessException("房屋未上架，不能申请");
        }
        leaseOrder.setOrderNo(generateOrderNo());
        leaseOrder.setStatus(0);
        leaseOrder.setMonthRent(house.getRentPrice());
        leaseOrder.setDeposit(house.getDeposit());
        LocalDate endDate = leaseOrder.getStartDate().plusMonths(leaseOrder.getLeaseMonths());
        leaseOrder.setEndDate(endDate);
        if (house.getRentPrice() != null) {
            leaseOrder.setTotalAmount(house.getRentPrice().multiply(new BigDecimal(leaseOrder.getLeaseMonths())));
        }
        save(leaseOrder);
    }

    private void fillHouseTitle(List<LeaseOrder> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            List<House> allHouses = houseService.list();
            Map<Long, String> houseMap = allHouses.stream()
                    .collect(Collectors.toMap(House::getId, House::getTitle, (a, b) -> a));
            for (LeaseOrder order : list) {
                if (order.getHouseId() != null && houseMap.containsKey(order.getHouseId())) {
                    order.setHouseTitle(houseMap.get(order.getHouseId()));
                }
            }
        } catch (Exception e) {
        }
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        return "ORD" + timestamp + String.format("%04d", randomNum);
    }

}
