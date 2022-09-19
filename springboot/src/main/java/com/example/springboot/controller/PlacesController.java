package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Device;
import com.example.springboot.entity.DmUser;
import com.example.springboot.entity.Places;
import com.example.springboot.mapper.DeviceMapper;
import com.example.springboot.mapper.PlacesMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlacesController {
    @Resource
    private PlacesMapper placesMapper;

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        placesMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result<?> insert(@RequestBody Places places){
        placesMapper.insert(places);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Places places){
        placesMapper.updateById(places);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id){
        placesMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/count")
    public Result<?> getCount() {
        LambdaQueryWrapper<Places> wrapper = Wrappers.<Places>lambdaQuery();
        Long placeCount = placesMapper.selectCount(wrapper);
        return Result.success(placeCount);
    }

    @GetMapping("/all")
    public Result<?> getAll(@RequestParam Integer ptype) {
        LambdaQueryWrapper<Places> wrapper = Wrappers.<Places>lambdaQuery();
        wrapper.select(Places::getPlaceId, Places::getName);
        wrapper.eq(Places::getPtype,ptype);
        List<Places> placesList = placesMapper.selectList(wrapper);
        return Result.success(placesList);
    }
    @GetMapping("/{ptype}")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @PathVariable Integer ptype){
        LambdaQueryWrapper<Places> wrapper = Wrappers.<Places>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Places::getName, search);
        }
        wrapper.orderByAsc(Places::getPlaceId);
        wrapper.eq(Places::getPtype,ptype);
        Page<Places> placesPage = placesMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(placesPage);
    }

}