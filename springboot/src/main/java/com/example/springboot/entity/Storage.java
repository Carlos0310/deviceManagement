package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@TableName("storage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Storage {
    @TableId(type = IdType.AUTO)
    private Integer storageId;
    private Integer deviceId;
    private Integer placeId;
    private Integer num;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;
}