package com.houserental.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.houserental.entity.House;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HouseMapper extends BaseMapper<House> {

}
