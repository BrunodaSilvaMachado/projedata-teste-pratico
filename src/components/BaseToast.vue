<script setup>
defineProps({
  show: Boolean,
  message: String,
  type: String
})
</script>

<template>
  <div v-if="show" :class="['toast', type]">
    {{ message }}
  </div>
</template>

<style scoped>
.toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 0.8rem 1.2rem;
  border-radius: 8px;
  color: white;
  font-weight: 500;
  z-index: 2000;
}

.success {
  background: #10b981;
}

.error {
  background: #ef4444;
}
</style>
