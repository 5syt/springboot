package com.houserental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houserental.entity.HouseType;
import com.houserental.exception.BusinessException;
import com.houserental.mapper.HouseTypeMapper;
import com.houserental.service.HouseTypeService;
import com.houserental.utils.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HouseTypeServiceImpl extends ServiceImpl<HouseTypeMapper, HouseType> implements HouseTypeService {

    private static final String CACHE_KEY = "house:type:list";

    @Resource
    private RedisUtils redisUtils;

    @Override
    public IPage<HouseType> pageType(Long pageNum, Long pageSize, String name) {
        LambdaQueryWrapper<HouseType> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(HouseType::getName, name);
        }
        wrapper.orderByAsc(HouseType::getSort);
        wrapper.orderByDesc(HouseType::getCreateTime);
        Page<HouseType> page = new Page<>(pageNum, pageSize);
        return page(page, wrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HouseType> listAllType() {
        Object cacheObj = redisUtils.get(CACHE_KEY);
        if (cacheObj != null) {
            return (List<HouseType>) cacheObj;
        }
        LambdaQueryWrapper<HouseType> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(HouseType::getSort);
        wrapper.orderByDesc(HouseType::getCreateTime);
        List<HouseType> list = list(wrapper);
        redisUtils.set(CACHE_KEY, list);
        return list;
    }

    @Override
    public void addType(HouseType houseType) {
        HouseType existType = getByName(houseType.getName());
        if (existType != null) {
            throw new BusinessException("类型名称已存在");
        }
        if (houseType.getSort() == null) {
            houseType.setSort(0);
        }
        save(houseType);
        redisUtils.delete(CACHE_KEY);
    }

    @Override
    public void updateType(HouseType houseType) {
        if (houseType.getId() == null) {
            throw new BusinessException("类型ID不能为空");
        }
        HouseType existType = getById(houseType.getId());
        if (existType == null) {
            throw new BusinessException("类型不存在");
        }
        HouseType sameNameType = getByName(houseType.getName());
        if (sameNameType != null && !sameNameType.getId().equals(houseType.getId())) {
            throw new BusinessException("类型名称已存在");
        }
        updateById(houseType);
        redisUtils.delete(CACHE_KEY);
    }

    @Override
    public void deleteType(Long id) {
        HouseType type = getById(id);
        if (type == null) {
            throw new BusinessException("类型不存在");
        }
        removeById(id);
        redisUtils.delete(CACHE_KEY);
    }

    private HouseType getByName(String name) {
        LambdaQueryWrapper<HouseType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HouseType::getName, name);
        return getOne(wrapper);
    }

}
