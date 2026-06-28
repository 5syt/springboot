package com.houserental.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("house")
public class House extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String title;

    private Long typeId;

    private BigDecimal area;

    private String rooms;

    private BigDecimal rentPrice;

    private BigDecimal deposit;

    private String address;

    private String city;

    private String district;

    private String floor;

    private String orientation;

    private String decoration;

    private String description;

    private String images;

    private Integer status;

    private Long createBy;

}
