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
      <el-table-column prop="deviceId" label="设备编号" sortable min-width="80"/>
      <el-table-column prop="name" label="设备名称"  min-width="90"/>
      <el-table-column prop="price" label="设备单价"  min-width="80"/>
      <el-table-column prop="num" label="设备数量"  min-width="80"/>
      <el-table-column prop="status" label="设备状态"  min-width="80"/>
      <el-table-column prop="manufactureDate" label="生产日期"  min-width="100"/>
      <el-table-column prop="expirationDate" label="保修期" min-width="100"/>
      <el-table-column prop="details" label="详情"  min-width="120"/>
      <el-table-column label="图片">
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.img"
              :preview-src-list="[scope.row.img]"
              :initial-index="1">
          </el-image>
        </template>
      </el-table-column>
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
                @confirm="handleDelete(scope.row.deviceId)">
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
        <el-form-item label="设备编号" v-if="form.deviceId">
          <el-input v-model="form.deviceId" style="width: 80%" disabled ></el-input>
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="form.name" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="设备单价">
          <el-input v-model="form.price" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="设备数量">
          <el-input v-model="form.num" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="生产日期">
          <el-date-picker v-model="form.manufactureDate" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable></el-date-picker>
        </el-form-item>
        <el-form-item label="保质期">
          <el-date-picker v-model="form.expirationDate" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable></el-date-picker>
        </el-form-item>
        <el-form-item label="详情">
          <el-input v-model="form.details" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
              ref="upload"
              class="upload-demo"
              action="/api/files/upload"
              :limit="1"
              :on-success="filesUploadSuccess">
            <el-button type="primary">点击上传</el-button>
          </el-upload>
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
  name: 'Device',
  components:{
    Edit,Delete
  },
  data() {
    return {
      user: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 5,
      total: 10,
      form: {},
      tableData: [],
      ids:[]
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
    handleSelectionChange(val) {
      this.ids = val.map(v => v.deviceId)   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/device/deleteBatch", this.ids).then(res => {
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
      request.get('/device',{
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
      this.$nextTick(()=>{    //处理未来元素
        this.$refs['upload'].clearFiles();
      })
    },
    save() {
      if(this.form.deviceId){
        request.put("/device", this.form).then(res => {
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
        request.post("/device", this.form).then(res => {
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
      this.$nextTick(()=>{
        this.$refs['upload'].clearFiles();
      })
    },
    handleDelete(id) {
      request.delete('/device/'+id).then(res =>{
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
