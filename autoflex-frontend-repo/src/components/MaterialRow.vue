<script setup>
defineProps({
  mat: Object,
  available: Array,
})
</script>
<template>
  <div class="material-row">
    <select v-model="mat.rawMaterialId">
      <option disabled :value="null">Selecione</option>
      <option v-for="m in available" :key="m.id" :value="m.id">
        {{ m.name }}
      </option>
    </select>

    <input type="number" v-model.number="mat.quantityRequired" min="1" />

    <button @click="$emit('remove')">❌</button>
  </div>
</template>
<style scoped>
.material-row {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  margin-bottom: 0.5rem;
}

.material-row select,
.material-row input {
  flex: 1;
}
</style>
