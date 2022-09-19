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

@TableName("dm_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DmUser {
    @TableId(type = IdType.NONE)
    private Integer userId;
    private String name;
    private String sex;
    private String phone;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date inDate;
    private String address;
    private Integer role;
    private String password;
    private Integer firstIn;
}