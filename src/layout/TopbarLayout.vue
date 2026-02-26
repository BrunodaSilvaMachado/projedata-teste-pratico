<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()

const titles = {
  '/production': 'Sugestão de Produção',
  '/products': 'Gestão de Produtos',
  '/raw-materials': 'Gestão de Matérias-Primas'
}

const emit = defineEmits(['toggleSidebar'])
</script>

<template>
  <div class="topbar">
    <button class="menu-btn" @click="emit('toggleSidebar')">
      ☰
  </button>
    <div class="title">
      {{ titles[route.path] }}
    </div>

    <div class="user">
      👤 Usuário
    </div>
  </div>
</template>

<style scoped>
.topbar {
  background: white;
  padding: 1.2rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.title {
  font-size: 1.4rem;
  font-weight: 600;
}

.user {
  font-size: 0.9rem;
  color: #64748b;
}

.menu-btn {
  font-size: 1.4rem;
  background: none;
  border: none;
  cursor: pointer;
}

</style>
