package com.houserental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.houserental.entity.HouseType;

import java.util.List;

public interface HouseTypeService extends IService<HouseType> {

    IPage<HouseType> pageType(Long pageNum, Long pageSize, String name);

    List<HouseType> listAllType();

    void addType(HouseType houseType);

    void updateType(HouseType houseType);

    void deleteType(Long id);

}
