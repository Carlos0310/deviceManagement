<template>
  <div style="width: 50%">
    <div class="noticeTitle">最新公告</div>
    <el-button style="margin:20px" type="primary" @click="add">新增</el-button>
    <div v-for="item in noticeHeadsList" style="display: flex;">
      <div class="noticeItemTitle" @click="noticeClick(item)">
        <div>{{item.title}}</div>
        <div>{{item.releaseDate}}</div>
      </div>
      <el-popconfirm
          confirm-button-text="确认"
          cancel-button-text="取消"
          icon-color="red"
          title="确认删除吗?"
          @confirm="noticeDelete(item.noticeId)">
        <template #reference>
          <div style="margin-left: 10px">
              <el-button type="danger" size="mini">删除</el-button>
          </div>
        </template>
      </el-popconfirm>
    </div>
    <el-dialog v-model="NoticeDialogVisible" title="公告" width="40%">
      <div style="color: black;font-size: 18px;font-weight: bold;margin-bottom: 10px;">{{this.clickedNotice.title}}</div>
      <div style="color: grey;font-size: 15px;margin: 12px;">{{this.clickedNotice.author}}</div>
      <div style="color: grey;font-size: 20px;margin: 20px;">{{this.clickedNotice.noticeContent}}</div>
      <div style="color: grey;font-size: 15px;padding-right: 0;">{{this.clickedNotice.releaseDate}}</div>
    </el-dialog>
    <el-dialog v-model="EditorDialogVisible" title="公告" width="40%">
      标题:<el-input style="margin:20px;width: 50%" v-model="noticeForm.title"/>
      <el-input style="margin:20px;width: 80%" v-model="noticeForm.noticeContent" :rows="10" type="textarea"/>
      <el-button style="margin:20px" type="primary" @click="release">发布</el-button>
    </el-dialog>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
import {Delete} from '@element-plus/icons'

export default {
  name: "Notice",
  components:{
    Delete
  },
  data() {
    return {
      noticeForm:{
        title:"",
        author:"",
        noticeContent:"",
        releaseDate:""
      },
      noticeHeadsList:[],
      clickedNotice:{},
      NoticeDialogVisible:false,
      EditorDialogVisible:false,
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.noticeForm.author = JSON.parse(str).name
    this.getNotices();
  },
  methods: {
    release(){
      this.noticeForm.releaseDate=new Date();
      request.post("/notice",this.noticeForm).then(res=>{
        if(res.code==='0'){
          this.EditorDialogVisible=false;
          this.getNotices()
        }
      })
    },
    add(){
      this.EditorDialogVisible=true
    },
    noticeDelete(noticeId){
      request.delete('/notice/'+noticeId).then(res => {
        if (res.code === '0') {
          ElMessage({
            message: '删除成功',
            type: 'success',
          })
          this.getNotices();
        } else {
          ElMessage({
            message: res.msg,
            type: 'error',
          })
        }
      })
    },
    noticeClick(notice){
      this.clickedNotice=notice;
      this.NoticeDialogVisible=true;
    },
    getNotices(){
      request.get("/notice/all").then(res=>{
        if(res.code==='0'){
          this.noticeHeadsList=res.data;
          console.log(this.noticeHeadsList)
        }else{
          this.emptyNotice=true;
        }
      })
    },
  }
}
</script>

<style>
.noticeTitle{
  font-size:28px;
  font-weight: bold;
  padding: 20px;
}
.noticeItemTitle{
  width:90%;
  font-size:16px;
  line-height: 30px;
  padding: 5px 30px;
  text-decoration: underline;
  display:flex;
  justify-content: space-between;
}
.noticeItemTitle:hover{
  font-size:18px;
  line-height: 30px;
  font-weight: bold;
}
</style>
