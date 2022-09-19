package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Storage;
import com.example.springboot.mapper.StorageMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/storage")
public class StorageController {
    @Resource
    private StorageMapper storageMapper;

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        storageMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result<?> insert(@RequestBody Storage storage) {
        storageMapper.insert(storage);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Storage storage) {
        storageMapper.updateById(storage);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        storageMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Storage> wrapper = Wrappers.<Storage>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Storage::getStorageId, search);
        }
        wrapper.orderByAsc(Storage::getStorageId);
        Page<Storage> storagePage = storageMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(storagePage);
    }

}