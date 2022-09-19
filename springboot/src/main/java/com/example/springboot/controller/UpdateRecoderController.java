package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.UpdateRecorder;
import com.example.springboot.mapper.UpdateRecorderMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/updateRecorder")
public class UpdateRecoderController {
    @Resource
    private UpdateRecorderMapper updateRecorderMapper;



    @PostMapping
    public Result<?> initUpdRecorder(){
        updateRecorderMapper.initUpdRecorder();
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<UpdateRecorder> wrapper = Wrappers.<UpdateRecorder>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(UpdateRecorder::getUpdTable, search);
        }
        Page<UpdateRecorder> updateRecorderPage = updateRecorderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(updateRecorderPage);
    }

}