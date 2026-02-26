<script setup>
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const navigate = (path) => {
  router.push(path)
}

const isActive = (path) => route.path === path
</script>

<template>
  <div class="sidebar">
    <div class="logo">
      🚗 AutoFlex
    </div>

    <nav>
      <div
        class="menu-item"
        :class="{ active: isActive('/production') }"
        @click="navigate('/production')"
      >
        📊 Produção
      </div>

      <div
        class="menu-item"
        :class="{ active: isActive('/products') }"
        @click="navigate('/products')"
      >
        📦 Produtos
      </div>

      <div
        class="menu-item"
        :class="{ active: isActive('/raw-materials') }"
        @click="navigate('/raw-materials')"
      >
        🏗️ Matérias-Primas
      </div>
    </nav>
  </div>
</template>

<style scoped>
.sidebar {
  width: 260px;
  background: linear-gradient(180deg, #1e293b, #0f172a);
  color: white;
  padding: 2rem 1.5rem;
  display: flex;
  flex-direction: column;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 3rem;
}

nav {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.menu-item {
  padding: 0.9rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.25s ease;
}

.menu-item:hover {
  background-color: #334155;
  transform: translateX(4px);
}

.menu-item.active {
  background-color: #3b82f6;
}
</style>
