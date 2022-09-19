package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@TableName("update_recorder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecorder {
    @TableId(type = IdType.AUTO)
    private Integer updId;
    private String updTable;
    private String updType;
    private String updOld;
    private String updNew;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updTime;
}