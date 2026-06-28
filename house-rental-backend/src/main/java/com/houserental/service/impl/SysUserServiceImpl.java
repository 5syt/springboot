package com.houserental.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houserental.entity.SysRole;
import com.houserental.entity.SysUser;
import com.houserental.entity.SysUserRole;
import com.houserental.exception.BusinessException;
import com.houserental.mapper.SysRoleMapper;
import com.houserental.mapper.SysUserMapper;
import com.houserental.mapper.SysUserRoleMapper;
import com.houserental.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public IPage<SysUser> pageUser(Long pageNum, Long pageSize, String username, Integer status) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        return page(page, wrapper);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public List<SysRole> getUserRoles(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            return List.of();
        }
        return sysRoleMapper.selectBatchIds(roleIds);
    }

    @Override
    public void addUser(SysUser sysUser) {
        SysUser existUser = getUserByUsername(sysUser.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        if (sysUser.getPassword() == null || sysUser.getPassword().isEmpty()) {
            sysUser.setPassword("123456");
        }
        if (sysUser.getStatus() == null) {
            sysUser.setStatus(1);
        }
        save(sysUser);
    }

    @Override
    public void updateUser(SysUser sysUser) {
        if (sysUser.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        SysUser existUser = getById(sysUser.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }
        sysUser.setPassword(null);
        updateById(sysUser);
    }

    @Override
    public void deleteUser(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        removeById(id);
    }

    @Override
    public void resetPassword(Long id, String password) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        user.setPassword(password);
        updateById(user);
    }

    @Override
    public List<SysUser> listUser() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getStatus, 1);
        wrapper.orderByAsc(SysUser::getUsername);
        List<SysUser> list = list(wrapper);
        list.forEach(user -> user.setPassword(null));
        return list;
    }

}
