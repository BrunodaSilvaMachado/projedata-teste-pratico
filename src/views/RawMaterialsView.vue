<script setup>
import { ref, onMounted } from 'vue'
import rawMaterialService from '../services/rawMaterialService'

const rawMaterials = ref([])

onMounted(async () => {
  try {
    const response = await rawMaterialService.getAll()
    rawMaterials.value = response.data
  } catch (error) {
    console.error('Erro ao buscar materiais:', error)
  }
})
</script>

<template>
  <div>
    <h1>Matérias-Primas</h1>

    <ul>
      <li v-for="material in rawMaterials" :key="material.id">
        {{ material.name }} - Código {{ material.code }} - Quantidade {{ material.stockQuantity }} - unidade {{ material.unit }}
      </li>
    </ul>
  </div>
</template>
