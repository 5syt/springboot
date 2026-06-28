package com.houserental.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("house_type")
public class HouseType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private Integer sort;

}
