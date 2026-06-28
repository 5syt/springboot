package com.houserental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houserental.entity.House;
import com.houserental.exception.BusinessException;
import com.houserental.mapper.HouseMapper;
import com.houserental.service.HouseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Override
    public IPage<House> pageHouse(Long pageNum, Long pageSize, Long typeId, BigDecimal minPrice, BigDecimal maxPrice, Integer status, String keyword) {
        LambdaQueryWrapper<House> wrapper = new LambdaQueryWrapper<>();
        if (typeId != null) {
            wrapper.eq(House::getTypeId, typeId);
        }
        if (minPrice != null) {
            wrapper.ge(House::getRentPrice, minPrice);
        }
        if (maxPrice != null) {
            wrapper.le(House::getRentPrice, maxPrice);
        }
        if (status != null) {
            wrapper.eq(House::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(House::getTitle, keyword).or().like(House::getAddress, keyword));
        }
        wrapper.orderByDesc(House::getCreateTime);
        Page<House> page = new Page<>(pageNum, pageSize);
        return page(page, wrapper);
    }

    @Override
    public House getHouseDetail(Long id) {
        House house = getById(id);
        if (house == null) {
            throw new BusinessException("房屋不存在");
        }
        return house;
    }

    @Override
    public void addHouse(House house) {
        if (house.getStatus() == null) {
            house.setStatus(1);
        }
        save(house);
    }

    @Override
    public void updateHouse(House house) {
        if (house.getId() == null) {
            throw new BusinessException("房屋ID不能为空");
        }
        House existHouse = getById(house.getId());
        if (existHouse == null) {
            throw new BusinessException("房屋不存在");
        }
        updateById(house);
    }

    @Override
    public void deleteHouse(Long id) {
        House house = getById(id);
        if (house == null) {
            throw new BusinessException("房屋不存在");
        }
        removeById(id);
    }

    @Override
    public void toggleStatus(Long id) {
        House house = getById(id);
        if (house == null) {
            throw new BusinessException("房屋不存在");
        }
        Integer currentStatus = house.getStatus();
        Integer newStatus = (currentStatus == 0 || currentStatus == 2) ? 1 : 0;
        house.setStatus(newStatus);
        updateById(house);
    }

}
