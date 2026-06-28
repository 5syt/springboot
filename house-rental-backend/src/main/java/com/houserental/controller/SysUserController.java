package com.houserental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.houserental.common.Result;
import com.houserental.entity.SysRole;
import com.houserental.entity.SysUser;
import com.houserental.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @GetMapping("/page")
    public Result<IPage<SysUser>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        IPage<SysUser> page = sysUserService.pageUser(pageNum, pageSize, username, status);
        page.getRecords().forEach(user -> user.setPassword(null));
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PostMapping
    public Result<Void> add(@RequestBody SysUser sysUser) {
        sysUserService.addUser(sysUser);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody SysUser sysUser) {
        sysUserService.updateUser(sysUser);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return Result.success();
    }

    @PutMapping("/resetPwd")
    public Result<Void> resetPwd(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String password = params.get("password") != null ? params.get("password").toString() : null;
        sysUserService.resetPassword(id, password);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<SysUser>> list() {
        List<SysUser> list = sysUserService.listUser();
        return Result.success(list);
    }

    @GetMapping("/{id}/roles")
    public Result<List<SysRole>> getUserRoles(@PathVariable Long id) {
        List<SysRole> roles = sysUserService.getUserRoles(id);
        return Result.success(roles);
    }

}
