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
    <div style="margin:20px 10px;">
      <el-radio-group v-model="otype" @change="radioChange">
        <el-radio-button label="-1" size="medium">全部</el-radio-button>
        <el-radio-button label="1" size="medium">进货</el-radio-button>
        <el-radio-button label="0" size="medium">退货</el-radio-button>
      </el-radio-group>
    </div>
    <el-table :data="tableData" border style="width: 90%" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"/>
      <el-table-column prop="orderId" label="记录编号" sortable min-width="120"/>
      <el-table-column prop="supplierId" label="供应商号" min-width="90"/>
      <el-table-column prop="supplierName" label="供应商名称" min-width="110"/>
      <el-table-column prop="deviceName" label="设备名称" min-width="90"/>
      <el-table-column prop="num" label="数量" min-width="60"/>
      <el-table-column prop="date" label="日期" min-width="120"/>
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
                @confirm="handleDelete(scope.row.orderId)">
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
        <el-form-item label="记录编号" v-if="form.orderId">
          <el-input v-model="form.orderId" style="width: 80%" disabled ></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-radio v-model="form.otype" label="1">进货</el-radio>
          <el-radio v-model="form.otype" label="0">退货</el-radio>
        </el-form-item>
        <el-form-item label="供应商号">
          <el-select-v2
              v-model="form.supplierId"
              filterable
              :options="supplierOptions"
              placeholder=" "
              style="width: 80%"/>
        </el-form-item>
        <el-form-item label="设备编号">
          <el-select-v2
              v-model="form.deviceId"
              filterable
              :options="deviceOptions"
              placeholder=" "
              style="width: 80%"/>
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="form.num" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="form.date" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable></el-date-picker>
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
  name: 'Order',
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
      ids:[],
      otype:-1,
      supplierOptions:[],
      supplierList:{},
      deviceOptions:[],
      deviceList:{},
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    request.get("/user/" + this.user.userId).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })
    this.loadSupplierOptions()
    this.loadDeviceOptions()
    this.load()
  },
  methods: {
    loadSupplierOptions(){
      request.get('/supplier/all').then(res=>{
        res.data.forEach(item=>{
          this.supplierList[item.supplierId]=item.name
        })
        this.supplierOptions=res.data.map(suppliers=>({
          value : suppliers.supplierId,
          label : suppliers.name
        }))
      })
    },
    loadDeviceOptions(){
      request.get('/device/all').then(res=>{
        res.data.forEach(item=>{
          this.deviceList[item.deviceId]=item.name
        })
        this.deviceOptions=res.data.map(devices=>({
          value : devices.deviceId,
          label : devices.name
        }))
      })
    },
    radioChange(){
      this.load();
    },
    handleSelectionChange(val) {
      this.ids = val.map(v => v.orderId)   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/order/deleteBatch", this.ids).then(res => {
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
      request.get('/order/'+this.otype,{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.tableData=res.data.records
        this.tableData.forEach(item=>{
          item['supplierName']=this.supplierList[item.supplierId]
          item['deviceName']=this.deviceList[item.deviceId]
        })
        this.total=res.data.total
      })
    },
    add() {
      this.dialogVisible = true;
      this.form = {};
    },
    save() {
      if(this.form.orderId){
        request.put("/order", this.form).then(res => {
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
        request.post("/order", this.form).then(res => {
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
      request.delete('/order/'+id).then(res =>{
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
