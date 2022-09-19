package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Places;
import com.example.springboot.entity.Supplier;
import com.example.springboot.mapper.SupplierMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Resource
    private SupplierMapper supplierMapper;

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        supplierMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result<?> insert(@RequestBody Supplier supplier){
        supplierMapper.insert(supplier);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Supplier supplier){
        supplierMapper.updateById(supplier);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id){
        supplierMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/count")
    public Result<?> getCount(){
        LambdaQueryWrapper<Supplier> wrapper = Wrappers.<Supplier>lambdaQuery();
        Long supplierCount = supplierMapper.selectCount(wrapper);
        return Result.success(supplierCount);
    }

    @GetMapping("/all")
    public Result<?> getAll(){
        LambdaQueryWrapper<Supplier> wrapper = Wrappers.<Supplier>lambdaQuery();
        wrapper.select(Supplier::getSupplierId,Supplier::getName);
        List<Supplier> suppliers = supplierMapper.selectList(wrapper);
        return Result.success(suppliers);
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Supplier> wrapper = Wrappers.<Supplier>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Supplier::getName, search);
        }
        wrapper.orderByAsc(Supplier::getSupplierId);
        Page<Supplier> supplierPage = supplierMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(supplierPage);
    }

}