<template>
  <div class="homepage-hero-module">
    <div class="video-container">
      <div :style="fixStyle" class="filter">
        <div id="loginDiv" style="width: 400px; margin: 250px 700px; position:absolute;z-index:10;visibility: visible" >
          <div style="font-size: 30px; text-align: center; padding: 30px 0; color: #fff">欢迎登录设备管理系统</div>
          <el-form ref="LoginForm" :model="LoginForm" size="normal" :rules="LoginRules">
            <el-form-item prop="userId">
              <el-input v-model="LoginForm.userId" placeholder="请输入账号">
                <template #prefix>
                  <el-icon class="el-input__icon"><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="LoginForm.password" show-password placeholder="请输入密码">
                <template #prefix>
                  <el-icon class="el-input__icon"><key /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <div style="display: flex">
                <el-input prefix-icon="el-icon-key" v-model="LoginForm.validCode" style="width: 50%;" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon class="el-input__icon"><d-arrow-right /></el-icon>
                  </template>
                </el-input>
                <div style="background-color: white;margin:auto 15px;border-radius:4px;">
                  <ValidCode @input="createValidCode" />
                </div>
              </div>
            </el-form-item>
            <!--            <el-form-item>-->
            <!--              <el-radio v-model="form.role" :label="1" style="color: white">管理员</el-radio>-->
            <!--              <el-radio v-model="form.role" :label="2" style="color: white">普通用户</el-radio>-->
            <!--            </el-form-item>-->
            <el-form-item>
              <el-button style="width: 100%" type="primary" @click="login">登 录</el-button>
            </el-form-item>
            <el-form-item><el-button type="text" @click="showRegister">前往注册 >> </el-button></el-form-item>
          </el-form>
        </div>
        <div id="registerDiv"  style="width: 400px;margin: 250px 700px; position:absolute;z-index:20;visibility: hidden">
          <div style="color: #cccccc;font-size: 30px;text-align: center;padding: 30px;">欢迎注册</div>
          <el-form ref="RegisterForm" :model="RegisterForm" size="normal" :rules="RegisterRules">
            <el-form-item prop="userId">
              <el-input prefix-icon="" v-model="RegisterForm.userId">
                <template #prefix>
                  <el-icon class="el-input__icon"><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="RegisterForm.password" show-password>
                <template #prefix>
                  <el-icon class="el-input__icon"><key /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirm">
              <el-input v-model="RegisterForm.confirm" show-password>
                <template #prefix>
                  <el-icon class="el-input__icon"><key /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="role">
              <el-radio v-model="RegisterForm.role" :label="1" style="color: white">维修人员</el-radio>
              <el-radio v-model="RegisterForm.role" :label="2" style="color: white">实验人员</el-radio>
              <el-radio v-model="RegisterForm.role" :label="3" style="color: white">设备处</el-radio>
            </el-form-item>
            <el-form-item>
              <el-button style="width: 40%;margin:10px 20px" type="primary" @click="showLogin">返回登录</el-button>
              <el-button style="width: 40%;margin:10px 20px" type="primary" @click="register">注册</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <video :style="fixStyle" autoplay loop muted class="fillWidth" v-on:canplay="canplay">
        <source src="../assets/video1.mp4" type="video/mp4"/>
        浏览器不支持 video 标签，建议升级浏览器。
      </video>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";
import {User,Key,DArrowRight} from '@element-plus/icons'
import {ElMessage} from "element-plus";
export default {
  name: "Login",
  components: {
    User,Key,DArrowRight,
    ValidCode,
  },
  data() {
    return {
      vedioCanPlay: false,
      fixStyle: '',
      LoginForm: {role: 1},
      RegisterForm:{firstIn:1},
      LoginRules: {
        userId: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      },
      RegisterRules: {
        userId: [
          { required: true, message: '请输入用户名', trigger: 'blur'},
          { min: 4, max: 8, message: '长度为4-8个字符', trigger: 'blur'},
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 4, max: 8, message: '长度为4-8个字符', trigger: 'blur'},
        ],
        confirm: [
          { required: true, message: '请输入确认密码', trigger: 'blur'},
          { min: 4, max: 8, message: '长度为4-8个字符', trigger: 'blur'},
        ],
        role:[
          {required:true,message: '请选择人员',trigger: 'blur'}
        ]
      },
      validCode: ''
    }
  },
  mounted() {
    sessionStorage.removeItem("user")

    window.onresize = () => {
      const windowWidth = document.body.clientWidth
      const windowHeight = document.body.clientHeight
      const windowAspectRatio = windowHeight / windowWidth
      let videoWidth
      let videoHeight
      if (windowAspectRatio < 0.5625) {
        videoWidth = windowWidth
        videoHeight = videoWidth * 0.5625
        this.fixStyle = {
          height: windowWidth * 0.5625 + 'px',
          width: windowWidth + 'px',
          'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
          'margin-left': 'initial'
        }
      } else {
        videoHeight = windowHeight
        videoWidth = videoHeight / 0.5625
        this.fixStyle = {
          height: windowHeight + 'px',
          width: windowHeight / 0.5625 + 'px',
          'margin-left': (windowWidth - videoWidth) / 2 + 'px',
          'margin-bottom': 'initial'
        }
      }
    }
    window.onresize()
  },
  methods: {
    showRegister(){
      document.getElementById("loginDiv").style.visibility="hidden";
      document.getElementById("registerDiv").style.visibility="visible";
    },
    showLogin(){
      document.getElementById("loginDiv").style.visibility="visible";
      document.getElementById("registerDiv").style.visibility="hidden";
    },
    canplay() {
      this.vedioCanPlay = true
    },
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    login () {
      this.$refs['LoginForm'].validate((valid) => {
        if (valid) {
          if (!this.LoginForm.validCode) {
            this.$message.error("请填写验证码")
            return
          }
          if(this.LoginForm.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }
          request.post("/user/login", this.LoginForm).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "登录成功"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))  // 缓存用户信息

              // // 登录成功的时候更新当前路由
              // activeRouter()
              this.$router.push("/index")  //登录成功之后进行页面的跳转，跳转到主页

            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    },
    register(){
      this.$refs["RegisterForm"].validate((valid) => {
        if (valid) {
          if(this.RegisterForm.password!==this.RegisterForm.confirm){
            ElMessage({
              message: '两次密码不一致',
              type: 'error',
            })
            return
          }
          request.post('/user/register',this.RegisterForm).then(res => {
            if (res.code === '0') {
              ElMessage({
                message: '注册成功',
                type: 'success',
              })
              this.showLogin();
            } else {
              ElMessage({
                message: res.msg,
                type: 'error',
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.homepage-hero-module,
.video-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.video-container .poster img{
  z-index: 0;
  position: absolute;
}

.video-container .filter {
  z-index: 1;
  position: absolute;
  /*background: rgba(0, 0, 0, 0.4);*/
  width: 100%;
}

.fillWidth {
  width: 100%;
}
</style>
