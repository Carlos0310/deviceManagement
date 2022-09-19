package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@TableName("device")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @TableId(type = IdType.AUTO)
    private Integer deviceId;
    private String name;
    private Integer num;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date manufactureDate;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date expirationDate;
    private BigDecimal price;
    private int status;
    private String details;
    private String img;
    // @TableField(exist = false)
    // private String deviceList;
}