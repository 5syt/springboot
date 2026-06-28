package com.houserental.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String path;

    private String component;

    private String icon;

    private Long parentId;

    private Integer sort;

    private String perms;

    private Integer type;

}
