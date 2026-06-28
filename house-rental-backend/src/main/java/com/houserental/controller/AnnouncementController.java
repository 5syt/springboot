package com.houserental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.houserental.common.Result;
import com.houserental.entity.Announcement;
import com.houserental.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @GetMapping("/page")
    public Result<IPage<Announcement>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        IPage<Announcement> page = announcementService.pageAnnouncement(pageNum, pageSize, type, status);
        return Result.success(page);
    }

    @GetMapping("/list")
    public Result<List<Announcement>> list() {
        List<Announcement> list = announcementService.listPublished();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Announcement> getById(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncementDetail(id);
        return Result.success(announcement);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Announcement announcement) {
        announcementService.addAnnouncement(announcement);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Announcement announcement) {
        announcementService.updateAnnouncement(announcement);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        announcementService.toggleStatus(id);
        return Result.success();
    }

}
