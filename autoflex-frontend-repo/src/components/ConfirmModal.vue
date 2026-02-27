<script setup>
import BaseModal from './BaseModal.vue'

defineProps({
  show: Boolean,
  message: String,
  title: {
    type: String,
    default: 'Atenção',
  },
})

const emit = defineEmits(['confirm', 'cancel'])
</script>

<template>
  <BaseModal :show="show" :title="title" @close="emit('cancel')">
    <p>{{ message }}</p>
    <div class="actions">
      <button class="cancel" @click="emit('cancel')">Cancelar</button>
      <button class="danger" @click="emit('confirm')">Confirmar</button>
    </div>
  </BaseModal>
</template>

<style scoped>
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
