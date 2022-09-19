package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Device;
import com.example.springboot.entity.DmUser;
import com.example.springboot.entity.dto.UserDTO;
import com.example.springboot.mapper.DmUserMapper;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private DmUserMapper userMapper;

    @PostMapping("/register")
    public Result<?> register(@RequestBody DmUser dmUser) {
        System.out.println(dmUser);
        DmUser res = userMapper.selectOne(Wrappers.<DmUser>lambdaQuery().eq(DmUser::getUserId, dmUser.getUserId()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        if (dmUser.getPassword() == null) {
            dmUser.setPassword("123456");
        }
        userMapper.insert(dmUser);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody DmUser dmUser) {
        DmUser res = userMapper.selectOne(Wrappers.<DmUser>lambdaQuery().eq(DmUser::getUserId, dmUser.getUserId()).eq(DmUser::getPassword, dmUser.getPassword()));
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }
        return Result.success(res);
    }
    @PostMapping("/loginJwt")
    public Result<?> loginJwt(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        String username = userDTO.getUserId();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error("-1", "用户名或密码为空");
        }else{
            DmUser res = userMapper.selectOne(Wrappers.<DmUser>lambdaQuery()
                    .eq(DmUser::getUserId, userDTO.getUserId())
                    .eq(DmUser::getPassword, userDTO.getPassword()));
            if (res == null) {
                return Result.error("-1", "用户名或密码错误");
            }
            String token = TokenUtils.genToken(userDTO.getUserId(),userDTO.getPassword());
            userDTO.setToken(token);
            return Result.success(userDTO);
        }
    }

    @PostMapping
    public Result<?> save(@RequestBody DmUser dmUser) {
        if (dmUser.getPassword() == null) {
            dmUser.setPassword("123456");
        }
        userMapper.insert(dmUser);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody DmUser dmUser) {
        userMapper.updateById(dmUser);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(userMapper.selectById(id));
    }

    @GetMapping("/oneToMany")
    public Result<?> oneToManyFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "3") Integer pageSize,
                                       @RequestParam(defaultValue = "") String search) {
        Page<DmUser> userPage = userMapper.findPage(new Page<>(pageNum, pageSize));
        return Result.success(userPage);
    }

    @GetMapping("/all/{role}")
    public Result<?> getAll(@PathVariable Integer role) {
        LambdaQueryWrapper<DmUser> wrapper = Wrappers.<DmUser>lambdaQuery();
        wrapper.select(DmUser::getUserId, DmUser::getName);
        wrapper.eq(DmUser::getRole,role);
        List<DmUser> userList = userMapper.selectList(wrapper);
        return Result.success(userList);
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<DmUser> wrapper = Wrappers.<DmUser>lambdaQuery();
        if (StringUtils.isNotBlank(search)) {
            wrapper.like(DmUser::getUserId, search);
        }
        wrapper.orderByAsc(DmUser::getUserId);
        Page<DmUser> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        LambdaQueryWrapper<DmUser> wrapper = Wrappers.<DmUser>lambdaQuery();
        wrapper.orderByAsc(DmUser::getUserId);
        List<DmUser> list =userMapper.selectList(wrapper);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("userId","账户ID");
        writer.addHeaderAlias("name","名字");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("phone","联系方式");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("inDate","入职日期");
        writer.addHeaderAlias("role","角色");
        writer.addHeaderAlias("firstIn","新用户");

        writer.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;chartset=utf-8");
        String fileName= URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }

    @PostMapping("/import")
    public void imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("账户ID","userId");
        reader.addHeaderAlias("名字","name");
        reader.addHeaderAlias("性别","sex");
        reader.addHeaderAlias("联系方式","phone");
        reader.addHeaderAlias("年龄","age");
        reader.addHeaderAlias("地址","address");
        reader.addHeaderAlias("角色","role");
        List<DmUser> list = reader.readAll(DmUser.class);
        for(DmUser user:list){
            System.out.println(user);
            userMapper.insert(user);
        }
    }
}