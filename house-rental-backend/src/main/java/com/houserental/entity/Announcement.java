package com.houserental.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("announcement")
public class Announcement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String title;

    private String content;

    private Integer type;

    private Integer status;

    private Long createBy;

}
