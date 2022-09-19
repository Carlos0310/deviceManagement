package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Device;
import com.example.springboot.entity.DmUser;
import com.example.springboot.entity.Places;
import com.example.springboot.entity.Supplier;
import com.example.springboot.mapper.DeviceMapper;
import com.example.springboot.mapper.DmUserMapper;
import com.example.springboot.mapper.PlacesMapper;
import com.example.springboot.mapper.SupplierMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/count")
public class CountController {
    @Resource
    private PlacesMapper placesMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private DmUserMapper dmUserMapper;

    @GetMapping()
    public Result<?> getCount() {
        LambdaQueryWrapper<Device> dwrapper = Wrappers.<Device>lambdaQuery();
        Long deviceCount = deviceMapper.selectCount(dwrapper);
        LambdaQueryWrapper<Places> pwrapper = Wrappers.<Places>lambdaQuery();
        Long placesCount = placesMapper.selectCount(pwrapper);
        LambdaQueryWrapper<Supplier> swrapper = Wrappers.<Supplier>lambdaQuery();
        Long supplierCount = supplierMapper.selectCount(swrapper);
        ArrayList<Long> countMap = new ArrayList<>();
        countMap.add(deviceCount);
        countMap.add(placesCount);
        countMap.add(supplierCount);
        return Result.success(countMap);
    }
    @GetMapping("/user")
    public Result<?> getUserCount(){
        ArrayList<Long> userCountList=new ArrayList<>();
        for(int i=0;i<4;i++){
            LambdaQueryWrapper<DmUser> wrapper = Wrappers.<DmUser>lambdaQuery();
            wrapper.eq(DmUser::getRole,i);
            Long oneCount = dmUserMapper.selectCount(wrapper);
            userCountList.add(oneCount);
        }
        return Result.success(userCountList);
    }

}