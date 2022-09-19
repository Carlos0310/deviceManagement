<template>
  <div style="padding: 15px;width: 100%;overflow: scroll">
    <!--    功能区-->
    <div style="padding: 10px;display: flex">
      <el-button type="primary" @click="add" class="funcBtn">新增</el-button>
      <el-upload
          action="http://118.195.192.23:8081/user/import"
          :show-file-list="false"
          accept="xlsx"
          on-success="handleImportSuccess"
          class="funcBtn"
      >
        <el-button type="primary">导入<el-icon><Download /></el-icon></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="funcBtn">导出<el-icon><Upload /></el-icon></el-button>
    </div>
    <!--    搜索区-->
    <div style="padding: 10px">
      <el-input v-model="search" placeholder="请输入关键字" clearable style="width: 20%"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table :data="tableData" border style="width: 90%" stripe>
      <el-table-column prop="userId" label="账户ID" sortable min-width="110"/>
      <el-table-column prop="name" label="名称" min-width="100"/>
      <el-table-column prop="sex" label="性别" min-width="70"/>
      <el-table-column prop="age" label="年龄" min-width="70"/>
      <el-table-column prop="phone" label="联系方式" min-width="120"/>
      <el-table-column prop="address" label="地址" min-width="130"/>
      <el-table-column prop="inDate" label="入职日期" min-width="120"/>
      <el-table-column prop="role" label="角色" min-width="100">
        <template #default="scope">
          <span v-if="scope.row.role === 0">管理员</span>
          <span v-if="scope.row.role === 1">维修人员</span>
          <span v-if="scope.row.role === 2">实验人员</span>
          <span v-if="scope.row.role === 3">设备处</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="130">
        <template #default="scope">
          <div  style="display:flex">
            <el-tooltip class="item" effect="dark" content="编辑" placement="top">
              <el-button size="mini" type="warning" @click="handleEdit(scope.row)">
                <el-icon><edit /></el-icon>
              </el-button>
            </el-tooltip>
            <el-popconfirm
                confirm-button-text="确认"
                cancel-button-text="取消"
                icon-color="red"
                title="确认删除吗?"
                @confirm="handleDelete(scope.row.userId)">
              <template #reference>
                <div style="margin-left: 10px">
                  <el-tooltip class="item" effect="dark" content="删除" placement="top">
                    <el-button type="danger" size="mini">
                      <el-icon><delete /></el-icon>
                    </el-button>
                  </el-tooltip>
                </div>
              </template>
            </el-popconfirm>
          </div>
        </template>
      </el-table-column>
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
    <el-dialog
        v-model="dialogVisible"
        title="添加"
        width="40%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="账户ID">
          <el-input v-model="form.userId" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio v-model="form.sex" label="男">男</el-radio>
          <el-radio v-model="form.sex" label="女">女</el-radio>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker v-model="form.inDate" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable></el-date-picker>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="form.address" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.phone" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-radio v-model="form.role" label="0">管理员</el-radio>
          <el-radio v-model="form.role" label="1">维修人员</el-radio>
          <el-radio v-model="form.role" label="2">实验人员</el-radio>
          <el-radio v-model="form.role" label="3">设备处</el-radio>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
import {Edit,Delete,Download,Upload} from '@element-plus/icons'

const {devServer} = require("../../vue.config");

export default {
  name: 'User',
  components:{
    Edit,Delete,Download,Upload
  },
  data() {
    return {
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 10,
      form: {},
      tableData: [],
      method: 0,
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      request.get('/user',{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    add() {
      this.dialogVisible = true;
      this.form = {};
      this.method=1;
    },
    save() {
      if(this.method===2){
        console.log("put")
        request.put("/user", this.form).then(res => {
          console.log(res);
          if(res.code==='0'){
            ElMessage({
              message: '更新成功',
              type: 'success',
            })
          }else{
            ElMessage({
              message: res.msg,
              type: 'error',
            })
          }
          this.load();
          this.dialogVisible=false;
        })
      }else if(this.method===1){
        console.log("save")
        request.post("/user", this.form).then(res => {
          console.log(res);
          if(res.code==='0'){
            ElMessage({
              message: '新增成功',
              type: 'success',
            })
          }else{
            ElMessage({
              message: res.msg,
              type: 'error',
            })
          }
          this.load();
          this.dialogVisible=false;
        })
      }
    },
    handleEdit(row) {
      this.form=JSON.parse(JSON.stringify(row));
      this.dialogVisible = true;
      this.method=2;
    },
    handleDelete(id) {
      request.delete('/user/'+id).then(res =>{
        if(res.code==='0'){
          ElMessage({
            message: '删除成功',
            type: 'success',
          })
        }else{
          ElMessage({
            message: res.msg,
            type: 'error',
          })
        }
        this.load();
      })
    },
    handleSizeChange() {
      this.load();
    },
    handleCurrentChange() {
      this.load();
    },
    exp(){
      window.open(devServer.proxy['/api'].target+'user/export')
    },
    handleImportSuccess(){
      ElMessage({
        message: '导入成功',
        type: 'success',
      })
      this.load()
    }
  }
}
</script>

<style scoped>
.funcBtn{
  margin-left: 10px;
}
</style>