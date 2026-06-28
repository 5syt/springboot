package com.houserental.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.houserental.common.Result;
import com.houserental.entity.HouseType;
import com.houserental.service.HouseTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/house/type")
public class HouseTypeController {

    @Resource
    private HouseTypeService houseTypeService;

    @GetMapping("/page")
    public Result<IPage<HouseType>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String name) {
        IPage<HouseType> page = houseTypeService.pageType(pageNum, pageSize, name);
        return Result.success(page);
    }

    @GetMapping("/list")
    public Result<List<HouseType>> list() {
        List<HouseType> list = houseTypeService.listAllType();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<HouseType> getById(@PathVariable Long id) {
        HouseType houseType = houseTypeService.getById(id);
        return Result.success(houseType);
    }

    @PostMapping
    public Result<Void> add(@RequestBody HouseType houseType) {
        houseTypeService.addType(houseType);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody HouseType houseType) {
        houseTypeService.updateType(houseType);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        houseTypeService.deleteType(id);
        return Result.success();
    }

}
