<script setup>
defineProps({
  show: Boolean,
  title: String
})

const emit = defineEmits(['close'])
</script>

<template>
  <div v-if="show" class="overlay" @click.self="emit('close')">
    <div class="modal">
      <h2>{{ title }}</h2>
      <slot />
    </div>
  </div>
</template>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  width: 420px;
  max-width: 95%;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
}
</style>
