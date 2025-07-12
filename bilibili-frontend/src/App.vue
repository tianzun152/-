<template>
  <div id="app">
    <el-container>
      <el-header height="60px">
        <h1>B站视频数据管理</h1>
      </el-header>
      <el-main>
        <video-list/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import VideoList from './components/VideoList.vue'

export default {
  name: 'App',
  components: { VideoList }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  margin: 20px;
}
</style>