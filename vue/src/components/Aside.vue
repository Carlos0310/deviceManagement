<template>
  <el-menu
      v-if="user.role===0"
      style="width: 260px;min-height: calc(100vh - 50px);background-color:#545c64"
      :default-active="$route.path"
      :default-openeds="['1','2']"
      router
      class="el-menu-vertical-demo">
    <el-sub-menu index="1" >
      <template #title>
        <div class="el-sub-menu-item">
          <el-icon><location /></el-icon>
          <span>系统管理</span>
        </div>
      </template>
        <el-menu-item index="/order">进退货管理</el-menu-item>
        <el-menu-item index="/repair">维修管理</el-menu-item>
        <el-menu-item index="/allocate">分配管理</el-menu-item>
        <el-menu-item index="/storage">存放管理</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="2">
      <template #title>
        <div class="el-sub-menu-item">
          <el-icon><Document /></el-icon>
          <span>数据管理</span>
        </div>
      </template>
      <el-menu-item index="/user">用户管理</el-menu-item>
      <el-menu-item index="/device">设备管理</el-menu-item>
      <el-menu-item index="/places">地点管理</el-menu-item>
      <el-menu-item index="/supplier">供应商管理</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/updateRecorder">更新记录表</el-menu-item>
  </el-menu>
  <el-menu
      v-if="user.role===1"
      style="width: 260px;min-height: calc(100vh - 50px);background-color:#545c64"
      :default-active="$route.path"
      router
      class="el-menu-vertical-demo">
    <el-menu-item index="/repair">查看报修</el-menu-item>
    <el-menu-item index="/repair">提交维修</el-menu-item>
  </el-menu>
  <el-menu
      v-if="user.role===2"
      style="width: 260px;min-height: calc(100vh - 50px);background-color:#545c64"
      :default-active="$route.path"
      router
      class="el-menu-vertical-demo">
      <el-menu-item index="/repair">报修</el-menu-item>
      <el-menu-item index="/allocate">申请设备</el-menu-item>
  </el-menu>
  <el-menu
      v-if="user.role===3"
      style="width: 260px;min-height: calc(100vh - 50px);background-color:#545c64"
      :default-active="$route.path"
      router
      class="el-menu-vertical-demo">
    <el-menu-item index="/device">设备管理</el-menu-item>
    <el-menu-item index="/order">进退货管理</el-menu-item>
    <el-menu-item index="/storage">存放管理</el-menu-item>
    <el-menu-item index="/allocate">分配管理</el-menu-item>
  </el-menu>
</template>

<script>

import {Location, Document, Setting} from '@element-plus/icons'
import request from "../utils/request";

export default {
  name: "Aside",
  components:{
    Location,Document,Setting
  },
  data(){
    return{
      user: {}
    }
  },
  created() {
    let UserStr=sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(UserStr)

    request.get("/user/"+this.user.userId).then(res=>{
      if(res.code==='0'){
        this.user=res.data
      }
    })
  }
}
</script>

<style scoped>
.el-menu-item{
  background-color: #545c64;
  color: #cccccc;
}
.el-menu-item.is-active{
  background-color: #687078;
  color: #ffffff;
}
.el-menu-item:hover{
  background-color: #a5abb1;
  color: #4c5257;
}
.el-sub-menu-item{
  color: #cccccc;
}
.el-sub-menu{
  background-color:#565c61
}
</style>