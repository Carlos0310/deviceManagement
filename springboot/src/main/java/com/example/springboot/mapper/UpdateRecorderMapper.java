package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.UpdateRecorder;

public interface UpdateRecorderMapper extends BaseMapper<UpdateRecorder> {
    void initUpdRecorder();
}
