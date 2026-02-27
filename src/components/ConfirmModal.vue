<script setup>
defineProps({
  show: Boolean,
  message: String
})

const emit = defineEmits(['confirm', 'cancel'])
</script>

<template>
  <div v-if="show" class="overlay">
    <div class="modal">
      <p>{{ message }}</p>

      <div class="actions">
        <button @click="emit('cancel')">Cancelar</button>
        <button class="danger" @click="emit('confirm')">Confirmar</button>
      </div>
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
  z-index: 1100;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  width: 350px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

button.danger {
  background: #ef4444;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
}
</style>
