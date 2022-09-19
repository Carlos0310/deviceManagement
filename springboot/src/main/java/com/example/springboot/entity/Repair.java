package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@TableName("repair")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repair {
    @TableId(type = IdType.AUTO)
    private Integer repairId;
    private Integer deviceId;
    private Integer personId;
    private Integer num;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date reportDate;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date repairDate;
    private String repairDetail;
    private String repairResult;
}