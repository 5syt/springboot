package com.houserental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.houserental.common.Result;
import com.houserental.entity.House;
import com.houserental.service.HouseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/house")
public class HouseController {

    @Resource
    private HouseService houseService;

    @GetMapping("/info/page")
    public Result<IPage<House>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        IPage<House> page = houseService.pageHouse(pageNum, pageSize, typeId, minPrice, maxPrice, status, keyword);
        return Result.success(page);
    }

    @GetMapping("/info/{id}")
    public Result<House> getById(@PathVariable Long id) {
        House house = houseService.getHouseDetail(id);
        return Result.success(house);
    }

    @PostMapping("/info")
    public Result<Void> add(@RequestBody House house) {
        houseService.addHouse(house);
        return Result.success();
    }

    @PutMapping("/info")
    public Result<Void> update(@RequestBody House house) {
        houseService.updateHouse(house);
        return Result.success();
    }

    @DeleteMapping("/info/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return Result.success();
    }

    @PutMapping("/info/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        houseService.toggleStatus(id);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
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
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "house";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(uploadDir + File.separator + fileName);
        file.transferTo(destFile);
        String accessUrl = "/files/house/" + fileName;
        return Result.success(accessUrl);
    }

}
