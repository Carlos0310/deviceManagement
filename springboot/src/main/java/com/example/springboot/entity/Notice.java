package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@TableName("notice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @TableId(type = IdType.AUTO)
    private Integer noticeId;
    private String title;
    private String author;
    private String noticeContent;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date releaseDate;
}