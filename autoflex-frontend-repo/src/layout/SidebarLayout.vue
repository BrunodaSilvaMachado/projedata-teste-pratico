<script setup>
import { useRouter, useRoute } from 'vue-router'
import { computed } from 'vue'

const props = defineProps({
  isOpen: Boolean
})

const emit = defineEmits(['close'])

const router = useRouter()
const route = useRoute()

const navigate = (path) => {
  router.push(path)
  if (window.innerWidth < 768) {
    emit('close')
  }
}

const isActive = (path) => route.path === path
</script>

<template>
  <div>
    <!-- Overlay Mobile -->
    <div
      v-if="isOpen"
      class="overlay"
      @click="emit('close')"
    ></div>

    <aside
      class="sidebar"
      :class="{ open: isOpen }"
    >
      <div class="logo">🚗 AutoFlex</div>

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
    </aside>
  </div>
</template>

<style scoped>
.sidebar {
  position: fixed;
  left: -300px;
  top: 0;
  width: 260px;
  height: 100%;
  background: linear-gradient(180deg, #1e293b, #0f172a);
  color: white;
  padding: 2rem 1.5rem;
  transition: left 0.3s ease;
  z-index: 1000;
}

.sidebar.open {
  left: 0;
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
}

.menu-item.active {
  background-color: #3b82f6;
}

/* Overlay */
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  z-index: 900;
}

/* Desktop */
@media (min-width: 768px) {
  .sidebar {
    left: 0;
    position: relative;
  }

  .overlay {
    display: none;
  }
}
</style>
