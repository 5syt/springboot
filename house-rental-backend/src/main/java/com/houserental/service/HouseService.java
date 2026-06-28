package com.houserental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.houserental.entity.House;

import java.math.BigDecimal;

public interface HouseService extends IService<House> {

    IPage<House> pageHouse(Long pageNum, Long pageSize, Long typeId, BigDecimal minPrice, BigDecimal maxPrice, Integer status, String keyword);

    House getHouseDetail(Long id);

    void addHouse(House house);

    void updateHouse(House house);

    void deleteHouse(Long id);

    void toggleStatus(Long id);

}
