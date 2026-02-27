<script setup>
import { ref } from 'vue'
import Sidebar from './SidebarLayout.vue'
import Topbar from './TopbarLayout.vue'

const isSidebarOpen = ref(true)

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value
}
</script>

<template>
  <div class="layout">
    <Sidebar :isOpen="isSidebarOpen"
      @close="isSidebarOpen = false"/>

    <div class="main">
      <Topbar @toggle-sidebar="toggleSidebar" />
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
