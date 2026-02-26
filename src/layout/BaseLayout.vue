<script setup>
import Sidebar from './SidebarLayout.vue'
import Topbar from './TopbarLayout.vue'
</script>

<template>
  <div class="layout">
    <Sidebar />

    <div class="main">
      <Topbar />
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  background-color: #f1f5f9;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.content {
  padding: 2rem;
  flex: 1;
  overflow-y: auto;
}
</style>
