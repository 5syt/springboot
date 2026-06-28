package com.houserental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.houserental.entity.SysRole;
import com.houserental.entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> pageUser(Long pageNum, Long pageSize, String username, Integer status);

    SysUser getUserByUsername(String username);

    List<SysRole> getUserRoles(Long userId);

    void addUser(SysUser sysUser);

    void updateUser(SysUser sysUser);

    void deleteUser(Long id);

    void resetPassword(Long id, String password);

    List<SysUser> listUser();

}
