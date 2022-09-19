package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Device;
import com.example.springboot.entity.Places;
import com.example.springboot.entity.Supplier;
import com.example.springboot.mapper.DeviceMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceMapper deviceMapper;

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        deviceMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result<?> save(@RequestBody Device device) {
        deviceMapper.insert(device);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Device device) {
        deviceMapper.updateById(device);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        deviceMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/count")
    public Result<?> getCount() {
        LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery();
        Long deviceCount = deviceMapper.selectCount(wrapper);
        return Result.success(deviceCount);
    }

    @GetMapping("/all")
    public Result<?> getAll() {
        LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery();
        wrapper.select(Device::getDeviceId, Device::getName);
        List<Device> deviceList = deviceMapper.selectList(wrapper);
        return Result.success(deviceList);
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Device::getName, search);
        }
        wrapper.orderByAsc(Device::getDeviceId);
        Page<Device> devicePage = deviceMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(devicePage);
    }

}