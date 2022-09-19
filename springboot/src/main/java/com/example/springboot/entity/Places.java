package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@TableName("places")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Places {
    @TableId(type = IdType.NONE)
    private Integer placeId;
    private Integer ptype;
    private String name;
    private String phone;
    private String place;
}