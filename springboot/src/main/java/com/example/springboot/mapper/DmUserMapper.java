package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.DmUser;
import org.apache.ibatis.annotations.Param;

public interface DmUserMapper extends BaseMapper<DmUser> {
    Page<DmUser> findPage(Page<DmUser> page);
}
