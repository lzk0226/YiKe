<template>
  <div class="app-container home">
    <el-row :gutter="20" justify="center" style="padding: 40px 0">
      <el-col :span="24" style="text-align: center">
        <h1>忆课后台管理</h1>
        <p>
          欢迎使用忆课后台管理系统。
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      version: '1.0.0' // 可根据实际修改
    }
  }
}
</script>

<style scoped>
.app-container.home {
  background-color: #f5f5f5;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
h1 {
  font-size: 36px;
  color: #409EFF;
  margin-bottom: 20px;
}
p {
  font-size: 16px;
  color: #606266;
}
</style>
