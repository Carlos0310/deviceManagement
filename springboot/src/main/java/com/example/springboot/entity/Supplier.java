package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@TableName("supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @TableId(type = IdType.AUTO)
    private Integer supplierId;
    private String name;
    private String phone;
    private String address;
}