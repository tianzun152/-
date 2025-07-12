<template>
  <el-dialog title="视频爬取"
             :visible="true"  @close="handleClose" :append-to-body="true" >
    <el-form>
      <el-form-item label="视频ID/URL">
        <el-input v-model="videoInput" placeholder="输入BV号/av号/完整URL"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">取消</el-button> <el-button type="primary" @click="handleCrawl">开始爬取</el-button>
    </span>
  </el-dialog>
</template>

<script>
import api from '@/services/api'

export default {
  // 移除了 visible prop
  data() {
    return {
      videoInput: '',
    }
  },
  methods: {
    async handleCrawl() {
      try {
        await api.crawlVideo(this.videoInput)
        this.$message.success('爬取成功')
        // 爬取成功后，触发 success 事件（父组件会刷新列表）
        this.$emit('success')
        // 并触发关闭事件，让父组件移除本组件
        this.$emit('close-dialog')
      } catch (error) {
        this.$message.error('爬取失败: ' + error.message)
        // 爬取失败时，也触发关闭事件，让父组件移除本组件
        this.$emit('close-dialog')
      }
    },
    handleClose() {
      // 当用户点击取消或对话框的关闭按钮时调用
      // 触发关闭事件，让父组件移除本组件
      this.$emit('close-dialog')
    }
  }
}
</script>