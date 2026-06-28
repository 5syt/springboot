package com.houserental.controller;

import com.houserental.common.Result;
import com.houserental.dto.LoginDTO;
import com.houserental.entity.SysMenu;
import com.houserental.entity.SysRole;
import com.houserental.entity.SysUser;
import com.houserental.entity.SysUserRole;
import com.houserental.mapper.SysMenuMapper;
import com.houserental.mapper.SysRoleMapper;
import com.houserental.mapper.SysUserRoleMapper;
import com.houserental.security.LoginUser;
import com.houserental.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        String token = jwtUtils.generateToken(sysUser.getId(), sysUser.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        sysUser.setPassword(null);
        data.put("userInfo", sysUser);

        return Result.success(data);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityContextHolder.clearContext();
        return Result.success();
    }

    @GetMapping("/userInfo")
    public Result<SysUser> getUserInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        sysUser.setPassword(null);
        return Result.success(sysUser);
    }

    @GetMapping("/menu")
    public Result<List<SysMenu>> getMenu() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getSysUser().getId();

        List<Long> roleIds = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        List<SysMenu> menus;
        boolean isAdmin = false;
        if (!roleIds.isEmpty()) {
            List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
            isAdmin = roles.stream().anyMatch(role -> "ADMIN".equals(role.getCode()));
        }

        if (isAdmin) {
            menus = sysMenuMapper.selectList(
                    new LambdaQueryWrapper<SysMenu>()
                            .in(SysMenu::getType, 0, 1)
                            .orderByAsc(SysMenu::getSort)
            );
        } else {
            menus = sysMenuMapper.selectList(
                    new LambdaQueryWrapper<SysMenu>()
                            .in(SysMenu::getType, 0, 1)
                            .orderByAsc(SysMenu::getSort)
            );
        }

        return Result.success(menus);
    }

}
