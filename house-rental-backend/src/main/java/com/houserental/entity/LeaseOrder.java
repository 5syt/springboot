package com.houserental.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private BigDecimal monthRent;

    private BigDecimal deposit;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal totalAmount;

    private Integer status;

    private String remark;

    @TableField(exist = false)
    private String houseTitle;

    @TableField(exist = false)
    private BigDecimal rent;

    public BigDecimal getRent() {
        return this.monthRent;
    }

    public void setRent(BigDecimal rent) {
        this.monthRent = rent;
    }

}
