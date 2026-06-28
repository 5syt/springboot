package com.houserental.controller;

import com.houserental.common.Result;
import com.houserental.entity.SysUser;
import com.houserental.exception.BusinessException;
import com.houserental.security.LoginUser;
import com.houserental.service.SysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String lowerSuffix = suffix.toLowerCase();
        if (!lowerSuffix.equals(".jpg") && !lowerSuffix.equals(".jpeg") && !lowerSuffix.equals(".png") && !lowerSuffix.equals(".gif")) {
            return Result.error("只支持 jpg、jpeg、png、gif 格式的图片");
        }
        long size = file.getSize();
        if (size > 5 * 1024 * 1024) {
            return Result.error("文件大小不能超过 5MB");
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "avatar";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(uploadDir + File.separator + fileName);
        file.transferTo(destFile);
        String accessUrl = "/files/avatar/" + fileName;

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getSysUser().getId();
        SysUser user = sysUserService.getById(userId);
        if (user != null) {
            user.setAvatar(accessUrl);
            sysUserService.updateById(user);
        }

        return Result.success(accessUrl);
    }

}
