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
          <el-button type="danger">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!--    搜索区-->
    <div style="padding: 10px">
      <el-input v-model="search" placeholder="请输入关键字" clearable style="width: 20%"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table :data="tableData" border style="width: 90%" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"/>
      <el-table-column prop="allocateId" label="记录编号" sortable min-width="120"/>
      <el-table-column prop="placeName" label="分配实验室" min-width="130"/>
      <el-table-column prop="personName" label="分配实验员工" min-width="140"/>
      <el-table-column prop="deviceName" label="设备名称" min-width="90"/>
      <el-table-column prop="num" label="数量" min-width="70"/>
      <el-table-column prop="personDate" label="申请分配日期" min-width="140" v-if="[0,2].includes(user.role)"/>
      <el-table-column prop="placeDate" label="分配设备日期" min-width="130" v-if="[0,3].includes(user.role)"/>
      <el-table-column fixed="right" label="操作" width="130">
        <template #default="scope"  >
          <div>
            <div style="display: flex;" v-if="user.role===0 || scope.row.placeDate===null">
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                <el-button size="mini" type="warning" @click="handleEdit(scope.row)">
                  <el-icon>
                    <edit/>
                  </el-icon>
                </el-button>
              </el-tooltip>
              <el-popconfirm
                  confirm-button-text="确认"
                  cancel-button-text="取消"
                  icon-color="red"
                  title="确认删除吗?"
                  @confirm="handleDelete(scope.row.allocateId)">
                <template #reference>
                  <div style="margin-left: 10px">
                    <el-tooltip class="item" effect="dark" content="删除" placement="top">
                      <el-button type="danger" size="mini">
                        <el-icon>
                          <delete/>
                        </el-icon>
                      </el-button>
                    </el-tooltip>
                  </div>
                </template>
              </el-popconfirm>
            </div>
            <el-button type="success" v-else-if="scope.row.placeDate!==null">已分配</el-button>
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
        <el-form-item label="记录编号" v-if="form.allocateId">
          <el-input v-model="form.allocateId" style="width: 80%" disabled></el-input>
        </el-form-item>
        <el-form-item label="地点" v-if="[0,2].includes(user.role)">
          <el-select-v2
              v-model="form.placeId"
              filterable
              :options="placeOptions"
              placeholder=" "
              style="width: 80%"/>
        </el-form-item>
        <el-form-item label="设备" v-if="[0,2].includes(user.role)">
          <el-select-v2
              v-model="form.deviceId"
              filterable
              :options="deviceOptions"
              placeholder=" "
              style="width: 80%"/>
        </el-form-item>
        <el-form-item label="分配实验员" v-if="[0,2].includes(user.role)">
          <el-select-v2
              v-model="form.personId"
              filterable
              :options="personOptions"
              placeholder=" "
              style="width: 80%"/>
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="form.num" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="分配设备日期" v-if="[0,3].includes(user.role)">
          <el-date-picker v-model="form.placeDate" value-format="YYYY-MM-DD" type="date" style="width: 80%"
                          clearable></el-date-picker>
        </el-form-item>
        <el-form-item label="申请分配日期" v-if="[0,2].includes(user.role)">
          <el-date-picker v-model="form.personDate" value-format="YYYY-MM-DD" type="date" style="width: 80%"
                          clearable></el-date-picker>
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
import {Edit, Delete} from '@element-plus/icons'

export default {
  name: 'Allocate',
  components: {
    Edit, Delete
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
      ids: [],
      placeOptions: [],
      placeList:{},
      deviceOptions: [],
      deviceList:{},
      personOptions:[],
      personList:{},
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
    this.loadPlaceOptions()
    this.loadDeviceOptions()
    this.loadPersonOptions()
    this.load()
  },
  methods: {
    loadPersonOptions() {
      request.get('/user/all/2').then(res => {
        res.data.forEach(item=>{
          this.personList[item.userId]=item.name
        })
        this.personOptions = res.data.map(personList => ({
          value: personList.userId,
          label: personList.name
        }))
      })
    },
    loadPlaceOptions() {
      request.get('/places/all', {
        params: {ptype: 0}
      }).then(res => {
        res.data.forEach(item=>{
          this.placeList[item.placeId]=item.name
        })
        this.placeOptions = res.data.map(placeList => ({
          value: placeList.placeId,
          label: placeList.name
        }))
      })
    },
    loadDeviceOptions() {
      request.get('/device/all').then(res => {
        res.data.forEach(item=>{
          this.deviceList[item.deviceId]=item.name
        })
        this.deviceOptions = res.data.map(deviceList => ({
          value: deviceList.deviceId,
          label: deviceList.name
        }))
      })
    },
    radioChange() {
      this.load();
    },
    handleSelectionChange(val) {
      this.ids = val.map(v => v.allocateId)   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/allocate/deleteBatch", this.ids).then(res => {
        if (res.code === '0') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    filesUploadSuccess(res) {
      this.form.img = res.data
    },
    load() {
      request.get('/allocate', {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.tableData = res.data.records
        this.tableData.forEach(item=>{
          item['personName']=this.personList[item.personId]
          item['placeName']=this.placeList[item.placeId]
          item['deviceName']=this.deviceList[item.deviceId]
        })
        this.total = res.data.total
      })
    },
    add() {
      this.dialogVisible = true;
      this.form = {};
    },
    save() {
      if (this.form.allocateId) {
        request.put("/allocate", this.form).then(res => {
          if (res.code === '0') {
            ElMessage({
              message: '更新成功',
              type: 'success',
            })
          } else {
            ElMessage({
              message: res.msg,
              type: 'error',
            })
          }
          this.load();
          this.dialogVisible = false;
        })
      } else {
        request.post("/allocate", this.form).then(res => {
          console.log(res);
          if (res.code === '0') {
            ElMessage({
              message: '新增成功',
              type: 'success',
            })
          } else {
            ElMessage({
              message: res.msg,
              type: 'error',
            })
          }
          this.load();
          this.dialogVisible = false;
        })
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogVisible = true;
    },
    handleDelete(id) {
      console.log(id)
      request.delete('/allocate/' + id).then(res => {
        if (res.code === '0') {
          ElMessage({
            message: '删除成功',
            type: 'success',
          })
        } else {
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
