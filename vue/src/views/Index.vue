<template>
  <div class="out">
    <div class="general" id="main"></div>
    <div class="general" id="main0">
      <div id="main01"></div>
      <div id="main02"></div>
      <div id="main03"></div>
    </div>
    <div class="block">
      <div class="general" id="main1"></div>
      <div class="general" id="notice">
        <div class="noticeHead">
          <div class="noticeTitle">最新公告</div>
          <div class="addNotice" @click="addNotice">更多</div>
        </div>
        <div v-for="item in noticeHeadsList">
          <div class="noticeItemTitle" @click="noticeClick(item)">
            <div>{{item.title}}</div>
            <div>{{item.releaseDate}}</div>
          </div>
        </div>
        <el-empty v-show="emptyNotice" description="还没有公告哦"></el-empty>
      </div>
    </div>
    <el-dialog v-model="NoticeDialogVisible" title="公告" width="40%">
      <div style="color: black;font-size: 18px;font-weight: bold;margin-bottom: 10px;">{{this.clickedNotice.title}}</div>
      <div style="color: grey;font-size: 15px;margin: 12px;">{{this.clickedNotice.author}}</div>
      <div style="color: grey;font-size: 20px;margin: 20px;">{{this.clickedNotice.noticeContent}}</div>
      <div style="color: grey;font-size: 15px;padding-right: 0px;">{{this.clickedNotice.releaseDate}}</div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import request from "../utils/request";
export default {
  name: "Index",
  data() {
    return{
      user:{},
      NoticeDialogVisible:false,
      clickedNotice:{},
      noticeHeadsList:[],
      emptyNotice:false,
      gaugeData1 : [
        {
          value: 1,
          name: '设备数',
          title: {
            offsetCenter: ['0%', '-30%']
          },
          detail: {
            valueAnimation: true,
            offsetCenter: ['0%', '5%']
          }
        }
      ],
      gaugeData2 : [
        {
          value: 0,
          name: '地点数',
          title: {
            offsetCenter: ['0%', '-30%']
          },
          detail: {
            valueAnimation: true,
            offsetCenter: ['0%', '5%']
          }
        }
      ],
      gaugeData3 : [
        {
          value: 0,
          name: '供应商',
          title: {
            offsetCenter: ['0%', '-30%']
          },
          detail: {
            valueAnimation: true,
            offsetCenter: ['0%', '5%']
          }
        }
      ],
      option : {
        graphic: {
          elements: [
            {
              type: 'text',
              left: 'center',
              top: 'center',
              style: {
                text: 'ECUST设备管理系统',
                fontSize: 80,
                fontWeight: 'bold',
                lineDash: [0, 200],
                lineDashOffset: 0,
                fill: 'transparent',
                stroke: '#000',
                lineWidth: 1
              },
              keyframeAnimation: {
                duration: 5000,
                loop: true,
                keyframes: [
                  {
                    percent: 0.7,
                    style: {
                      fill: 'transparent',
                      lineDashOffset: 200,
                      lineDash: [200, 0]
                    }
                  },
                  {
                    // Stop for a while.
                    percent: 0.8,
                    style: {
                      fill: 'transparent'
                    }
                  },
                  {
                    percent: 1,
                    style: {
                      fill: '#1e90ff'
                    }
                  }
                ]
              }
            }
          ]
        }
      },
      option0 : {
        series: [
          {
            type: 'gauge',
            startAngle: 90,
            endAngle: -270,
            animationDuration: 3000,
            max: 20,
            pointer: {
              show: false
            },
            progress: {
              show: true,
              overlap: false,
              roundCap: true,
              clip: false,
              itemStyle: {
                borderWidth: 1,
                borderColor: '#464646'
              }
            },
            axisLine: {
              lineStyle: {
                width: 20
              }
            },
            splitLine: {
              show: false,
              distance: 0,
              length: 10
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false,
              distance: 50
            },
            title: {
              fontSize: 20
            },
            detail: {
              width: 50,
              height: 18,
              fontSize: 20,
              color: 'auto',
              borderColor: 'auto',
              borderRadius: 20,
              borderWidth: 1,
              formatter: '{value}'
            }
          }
        ]
      },
      option1 : {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          bottom: '5%',
          left: 'center'
        },
        series: [
          {
            name: 'Person From',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              position: 'outside'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '40',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1, name: '管理人员' },
              { value: 3, name: '维修人员' },
              { value: 3, name: '实验人员' },
              { value: 1, name: '设备处人员' }
            ]
          }
        ]
      }
    }
  },
  created() {
    let UserStr=sessionStorage.getItem("user")||"{}"
    this.user=JSON.parse(UserStr)
    this.getNoticeHeads()
    this.getCounts();
    this.getUserCounts();
  },
  methods:{
    addNotice(){
      this.$router.push("/notice")
    },
    noticeClick(notice){
      this.clickedNotice=notice;
      this.NoticeDialogVisible=true;
    },
    getNoticeHeads(){
      request.get("/notice/heads").then(res=>{
        if(res.code==='0'){
          this.noticeHeadsList=res.data;
          console.log(this.noticeHeadsList)
        }else{
          this.emptyNotice=true;
        }
      })
    },
    getUserCounts(){
      request.get("/count/user").then(res => {
        if (res.code === '0') {
          let i=0;
          for(i=0;i<4;i++){
            this.option1.series[0].data[i].value=res.data[i];
          }
          this.initPersonForm();
        }
      })
    },
    getCounts(){
      request.get("/count").then(res => {
        if (res.code === '0') {
          this.gaugeData1[0].value = res.data[0];
          this.gaugeData2[0].value = res.data[1];
          this.gaugeData3[0].value = res.data[2];
          this.option0.series[0].max=Math.ceil(Math.max.apply(Math,res.data)/10)*10;
          this.initEcharts();
        }
      })
    },
    initEcharts(){
      let chartDom = document.getElementById('main');
      let myChart = echarts.init(chartDom);
      let chartDom01 = document.getElementById('main01');
      let myChart01 = echarts.init(chartDom01);
      let chartDom02 = document.getElementById('main02');
      let myChart02 = echarts.init(chartDom02);
      let chartDom03 = document.getElementById('main03');
      let myChart03 = echarts.init(chartDom03);
      myChart.setOption(this.option);
      myChart01.setOption(this.option0);
      myChart01.setOption({series: [{data: this.gaugeData1}]});
      // setInterval(function () {
      //   myChart01.setOption({series: [{data: gaugeData1}]});
      // }, 2000);
      myChart02.setOption(this.option0);
      myChart02.setOption({series: [{data: this.gaugeData2}]});
      myChart03.setOption(this.option0);
      myChart03.setOption({series: [{data: this.gaugeData3}]});
    },
    initPersonForm(){
      let chartDom1 = document.getElementById('main1');
      let myChart1 = echarts.init(chartDom1);
      myChart1.setOption(this.option1);
    }
  },
  mounted() {

  },
};
</script>

<style scoped>
.out{
  display: flex;
  flex-direction: column;
  width: 80%;
}
.block{
  display: flex;
  flex-direction: row;
}
.general{
  margin:20px;
  border-radius:10px;
  border:1px solid #eee;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
#main{
  width: 100%;
  height: 150px;
}
#main0{
  width: 100%;
  height: 200px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
#main01{
  height: 200px;
  width: 33.3%;
}
#main02{
  height: 200px;
  width: 33.3%;
}
#main03{
  height: 200px;
  width: 33.3%;
}
#main1 {
  height: 400px;
  width: 50%;
}
#notice {
  height: 400px;
  width: 50%;
}
.noticeHead{
  display:flex;
  justify-content: space-between;
}
.noticeTitle{
  font-size:28px;
  font-weight: bold;
  padding: 20px;
}
.addNotice{
  font-size:18px;
  color:#222;
  padding: 18px;
}
.noticeItemTitle{
  width:90%;
  font-size:16px;
  line-height: 18px;
  padding: 5px 30px;
  text-decoration: underline;
  display:flex;
  justify-content: space-between;
}
.noticeItemTitle:hover{
  font-size:18px;
  line-height: 18px;
  font-weight: bold;
}
</style>