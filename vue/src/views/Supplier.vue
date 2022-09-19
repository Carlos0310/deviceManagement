<template>
  <div style="padding: 15px;width: 100%;overflow: scroll">
    <!--    功能区-->
    <div style="padding: 10px">
      <el-button type="primary" @click="add">新增</el-button>
      <el-popconfirm
          v-if="user.role === 0"
          title="确定删除吗？"
          @confirm="deleteBatch"
      >
        <template #reference>
          <el-button type="danger" >批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!--    搜索区-->
    <div style="padding: 10px">
      <el-input v-model="search" placeholder="请输入关键字" clearable style="width: 20%"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table :data="tableData" border style="width: 90%" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"/>
      <el-table-column prop="supplierId" label="供应商" sortable min-width="70"/>
      <el-table-column prop="name" label="名称" min-width="130"/>
      <el-table-column prop="phone" label="联系方式" min-width="130"/>
      <el-table-column prop="address" label="地点" min-width="130"/>
      <el-table-column fixed="right" label="操作" width="130">
        <template #default="scope">
          <div style="display: flex;">
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
                @confirm="handleDelete(scope.row.supplierId)">
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
        <el-form-item label="地点编号" v-if="form.supplierId">
          <el-input v-model="form.supplierId" style="width: 80%" disabled ></el-input>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="form.name" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.phone" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="form.address" style="width: 80%"></el-input>
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
import {Edit,Delete} from '@element-plus/icons'

export default {
  name: 'Supplier',
  components:{
    Edit,Delete
  },
  data() {
    return {
      user: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 10,
      form: {},
      tableData: [],
      ids:[],
      ptype:0
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
    radioChange(){
      this.load();
    },
    handleSelectionChange(val) {
      this.ids = val.map(v => v.supplierId)   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/supplier/deleteBatch", this.ids).then(res => {
        if (res.code === '0') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    filesUploadSuccess(res){
      this.form.img=res.data
    },
    load(){
      request.get('/supplier',{
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
    },
    save() {
      if(this.form.supplierId){
        request.put("/supplier", this.form).then(res => {
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
      }else{
        request.post("/supplier", this.form).then(res => {
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
    },
    handleDelete(id) {
      console.log(id)
      request.delete('/supplier/'+id).then(res =>{
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
    }
  }
}
</script>
