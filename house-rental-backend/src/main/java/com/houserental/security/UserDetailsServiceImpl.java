package com.houserental.security;

import com.houserental.entity.SysMenu;
import com.houserental.entity.SysRole;
import com.houserental.entity.SysUser;
import com.houserental.entity.SysUserRole;
import com.houserental.mapper.SysMenuMapper;
import com.houserental.mapper.SysRoleMapper;
import com.houserental.mapper.SysUserMapper;
import com.houserental.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        List<String> permissions = getPermissions(sysUser.getId());

        return new LoginUser(sysUser, permissions);
    }

    private List<String> getPermissions(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
        List<String> roleCodes = roles.stream().map(SysRole::getCode).collect(Collectors.toList());

        List<SysMenu> menus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .isNotNull(SysMenu::getPerms)
                        .ne(SysMenu::getPerms, "")
        );

        List<String> perms = menus.stream()
                .map(SysMenu::getPerms)
                .collect(Collectors.toList());

        List<String> permissions = new ArrayList<>();
        permissions.addAll(roleCodes.stream().map(code -> "ROLE_" + code).collect(Collectors.toList()));
        permissions.addAll(perms);

        return permissions;
    }

}
