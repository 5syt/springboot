package com.houserental.controller;

import com.houserental.common.Result;
import com.houserental.entity.SysUser;
import com.houserental.exception.BusinessException;
import com.houserental.security.LoginUser;
import com.houserental.service.SysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Resource
    private SysUserService sysUserService;

    @GetMapping("/info")
    public Result<SysUser> getInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        sysUser.setPassword(null);
        return Result.success(sysUser);
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody SysUser sysUser) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getSysUser().getId();
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setNickname(sysUser.getNickname());
        user.setEmail(sysUser.getEmail());
        user.setPhone(sysUser.getPhone());
        user.setAvatar(sysUser.getAvatar());
        sysUserService.updateById(user);
        return Result.success();
    }

    @PutMapping("/updatePwd")
    public Result<Void> updatePwd(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new BusinessException("旧密码不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getSysUser().getId();
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!user.getPassword().equals(oldPassword)) {
            throw new BusinessException("旧密码不正确");
        }
        user.setPassword(newPassword);
        sysUserService.updateById(user);
        return Result.success();
    }

}
