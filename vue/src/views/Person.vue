<template>
  <div style="width: 90%">
    <el-card shadow="hover" style="min-width: 600px; max-width:800px;margin: 60px 80px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="账户ID">
          <el-input v-model="form.userId" style="width: 80%" disabled ></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" style="width: 80%"></el-input>
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
        <el-form-item label="地址">
          <el-input type="textarea" v-model="form.address" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker v-model="form.inDate" value-format="YYYY-MM-DD" type="date" style="width: 80%"
                          clearable></el-date-picker>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.phone" style="width: 80%"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card>

  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "Person",
  data() {
    return {
      form: {}
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    update() {
      this.form.firstIn=0
      request.put("/user", this.form).then(res => {
        if (res.code === '0') {
          ElMessage({
            type: "success",
            message: "更新成功"
          })
          sessionStorage.setItem("user", JSON.stringify(this.form))
          // 触发Layout更新用户信息
          this.$emit("userInfo")
          this.$router.push("/index");
        } else {
          ElMessage({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  }
}
</script>

<style>
</style>
