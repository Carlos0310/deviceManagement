package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@TableName("allocate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allocate {
    @TableId(type = IdType.AUTO)
    private Integer allocateId;
    private Integer personId;
    private Integer placeId;
    private Integer deviceId;
    private Integer num;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date placeDate;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date personDate;
}