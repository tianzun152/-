<template>
  <div class="video-container">
    <div class="action-bar">
      <el-button type="primary" @click="showCrawlerForm">
        <i class="el-icon-download"></i> 爬取视频
      </el-button>
      <el-input
          v-model="searchText"
          placeholder="搜索视频标题..."
          clearable
          style="width: 300px; margin-left: 20px"
      />
    </div>

    <el-table
        :data="filteredVideos"
        v-loading="loading"
        border
        style="width: 100%"
        empty-text="暂无数据"
    >
      <el-table-column prop="title" label="标题" width="300" />
      <el-table-column prop="upName" label="UP主" width="150" />
      <el-table-column prop="views" label="播放量" sortable />
      <el-table-column prop="coins" label="硬币数" sortable />
      <el-table-column prop="shares" label="分享数" sortable />
      <el-table-column prop="favorites" label="收藏数" sortable />
      <el-table-column prop="likes" label="点赞数" sortable />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button size="mini" type="danger" @click="deleteVideo(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalItems"
        style="margin-top: 20px"
    />

    <crawler-form
        v-if="showCrawlerDialog"
        @success="refreshVideoList"
        @close-dialog="showCrawlerDialog = false"
        key="crawler-form-component"
    ></crawler-form>
  </div>
</template>

<script>
import api from '@/services/api';
import CrawlerForm from '@/components/CrawlerForm.vue';

export default {
  components: {
    CrawlerForm
  },
  data() {
    return {
      videos: [],
      loading: false,
      searchText: '',
      currentPage: 1,
      pageSize: 10,
      showCrawlerDialog: false,
    };
  },
  computed: {
    filteredVideos() {
      return this.videos.filter(video => video.title.includes(this.searchText));
    },
    totalItems() {
      return this.filteredVideos.length;
    }
  },
  methods: {
    async fetchVideos() {
      this.loading = true;
      try {
        const response = await api.getVideos();
        console.log('后端返回的数据:', response);
        // 确保后端返回的数据对象中包含 'coin', 'share', 'favorite', 'views', 'likes', 'upName', 'title' 等属性
        this.videos = response;
      } catch (error) {
        this.$message.error('获取视频列表失败: ' + error.message);
      } finally {
        this.loading = false;
      }
    },
    showCrawlerForm() {
      console.log('按钮被点击了，正在尝试显示对话框');
      this.showCrawlerDialog = true;
      console.log('showCrawlerDialog 的值现在是:', this.showCrawlerDialog);
    },
    refreshVideoList() {
      this.fetchVideos();
      this.showCrawlerDialog = false;
    },
    async deleteVideo(id) {
      try {
        await api.deleteVideo(id);
        this.$message.success('删除成功');
        this.fetchVideos();
      } catch (error) {
        this.$message.error('删除失败: ' + error.message);
      }
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchVideos();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchVideos();
    }
  },
  created() {
    this.fetchVideos();
  }
};
</script>

<style scoped>
.video-container {
  padding: 20px;
}
.action-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
/* 你其他的样式 */
</style>