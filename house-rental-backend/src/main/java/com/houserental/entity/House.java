package com.houserental.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal area;

    private String rooms;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal rentPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
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

    @TableField(exist = false)
    private String typeName;

}
