package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Notice;
import com.example.springboot.mapper.NoticeMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeMapper noticeMapper;

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids) {
        noticeMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result<?> insert(@RequestBody Notice notice) {
        noticeMapper.insert(notice);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Notice notice) {
        noticeMapper.updateById(notice);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) {
        noticeMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping("/heads")
    public Result<?> getHeads(){
        LambdaQueryWrapper<Notice> wrapper = Wrappers.<Notice>lambdaQuery();
        wrapper.last("limit 8");
        List<Notice> notices = noticeMapper.selectList(wrapper);
        return Result.success(notices);
    }
    @GetMapping("/all")
    public Result<?> getAll(){
        LambdaQueryWrapper<Notice> wrapper = Wrappers.<Notice>lambdaQuery();
        List<Notice> notices = noticeMapper.selectList(wrapper);
        return Result.success(notices);
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Notice> wrapper = Wrappers.<Notice>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(Notice::getTitle, search);
        }
        wrapper.orderByAsc(Notice::getNoticeId);
        Page<Notice> noticePage = noticeMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(noticePage);
    }

}