package com.houserental.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lease_order")
public class LeaseOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String orderNo;

    private Long houseId;

    private Long tenantId;

    private String tenantName;

    private String tenantPhone;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal monthRent;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal deposit;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal totalAmount;

    private Integer status;

    private String remark;

    @TableField(exist = false)
    private String houseTitle;

    @TableField(exist = false)
    private Integer leaseMonths;

}
