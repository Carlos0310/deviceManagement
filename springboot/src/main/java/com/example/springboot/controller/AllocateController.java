package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Allocate;
import com.example.springboot.mapper.AllocateMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/allocate")
public class AllocateController {
    @Resource
    private AllocateMapper allocateMapper;

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        allocateMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result<?> insert(@RequestBody Allocate allocate) {
        allocateMapper.insert(allocate);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Allocate allocate) {
        allocateMapper.updateById(allocate);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        allocateMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Allocate> wrapper = Wrappers.<Allocate>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Allocate::getAllocateId, search);
        }
        wrapper.orderByAsc(Allocate::getAllocateId);
        Page<Allocate> allocatePage = allocateMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(allocatePage);
    }

}