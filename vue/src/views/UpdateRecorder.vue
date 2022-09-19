<template>
  <div style="padding: 15px;width: 100%;overflow: scroll">
    <div style="padding: 10px">
      <el-input v-model="search" placeholder="请输入关键字" clearable style="width: 20%"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="danger" style="margin-left: 5px" @click="initUpdRecorder">清空</el-button>
    </div>
    <el-table :data="tableData" border style="width: 90%" stripe>
<!--      <el-table-column prop="updId" label="id" sortable min-width="120"/>-->
      <el-table-column prop="updTable" label="更新的表" width="130"/>
      <el-table-column prop="updType" label="操作类型" width="130"/>
      <el-table-column prop="updOld" label="原始记录"  min-width="130"/>
      <el-table-column prop="updNew" label="新纪录" min-width="130"/>
    </el-table>
    <div style="margin:10px">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[3, 5, 10]"
          v-model:page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";

export default {
  name: 'UpdateRecorder',
  data() {
    return {
      user: {},
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 10,
      tableData:[]
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    // 请求服务端，确认当前登录用户的 合法信息
    request.get("/user/" + this.user.userId).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })
    this.load()
  },
  methods: {
    load(){
      request.get('/updateRecorder',{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.tableData=res.data.records
        this.total=res.data.total
        console.log(this.tableData)
      })
    },
    initUpdRecorder(){
      request.post('/updateRecorder').then(res=>{
        if(res.code==='0'){
          ElMessage({
            message: '清空成功',
            type: 'success',
          })
        }else {
          ElMessage({
            message: res.msg,
            type: 'error',
          })
        }
        this.load()
      })
    },
    handleSizeChange() {
      this.load();
    },
    handleCurrentChange() {
      this.load();
    }
  }
}
</script>
