package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Repair;
import com.example.springboot.mapper.RepairMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/repair")
public class RepairController {
    @Resource
    private RepairMapper repairMapper;

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        repairMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result<?> insert(@RequestBody Repair repair) {
        repairMapper.insert(repair);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Repair repair) {
        repairMapper.updateById(repair);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        repairMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Repair> wrapper = Wrappers.<Repair>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Repair::getRepairId, search);
        }
        wrapper.orderByAsc(Repair::getRepairId);
        Page<Repair> repairPage = repairMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(repairPage);
    }

}