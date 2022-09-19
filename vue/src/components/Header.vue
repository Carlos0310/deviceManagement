<template>
  <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #cccccc;display: flex;background-color:#545c64">
    <router-link to="/index">
      <div class="router">
        <div style="width: 50px;padding-left:25px;font-weight: bold;color: dodgerblue">
          <img src="../assets/logo.png" style="width:50px"/>
        </div>
        <div style="width: 200px;padding-left:30px;font-weight: bold;color: dodgerblue">设备管理系统</div>
      </div>
    </router-link>
    <div style="flex:1"></div>
    <div style="width: 100px;color:#cccccc">
      <span v-if="user.role === 0">管理员</span>
      <span v-if="user.role === 1">维修人员</span>
      <span v-if="user.role === 2">实验人员</span>
      <span v-if="user.role === 3">设备处</span></div>
    <div style="width: 100px">
      <el-dropdown>
        <span class="el-dropdown-link">
          {{ user.name }}
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>
            <el-dropdown-item divided @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <el-dialog
        v-model="dialogVisible"
        title="添加"
        width="30%">
      立即修改个人信息
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="gotoPerson">确认</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ArrowDown } from '@element-plus/icons'
import request from "../utils/request";
export default {
  name: "Header",
  components:{
    ArrowDown
  },
  data() {
    return {
      user:{},
      dialogVisible:false
    }
  },
  methods:{
    gotoPerson(){
      this.$router.push("/person");
      this.dialogVisible=false;
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
    if(this.user.firstIn===1){
      this.dialogVisible=true;
    }
  }
}
</script>

<style scoped>
.router{
  display: flex;
  flex-direction: row;
}
.el-dropdown-link{
  color: #cccccc;
}
</style>