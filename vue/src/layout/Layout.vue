<template>
<div>
  <Header :user="user"/>
  <div style="display: flex">
    <Aside/>
    <router-view @userInfo="refreshUser"/>
  </div>
</div>
</template>

<script>
import Header from "../components/Header";
import Aside from "../components/Aside";
import request from "../utils/request";
export default {
  name: "Layout",
  components: {
    Aside,
    Header
  },
  data() {
    return {
      user: {}
    }
  },
  created() {
    this.refreshUser()
  },
  methods: {
    refreshUser() {
      let userJson = sessionStorage.getItem("user");
      if (!userJson) {
        return
      }
      let userId = JSON.parse(userJson).userId
      request.get("/user/" + userId).then(res => {
        this.user = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>