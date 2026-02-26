<script setup>
import { ref, onMounted } from 'vue'
import productionService from '../services/productionService'

const productions = ref([])

onMounted(async () => {
  try {
    const response = await productionService.getSuggestion()
    productions.value = response.data
  } catch (error) {
    console.error('Erro ao buscar produções:', error)
  }
})
</script>

<template>
  <div>
    <h1>Produções</h1>

    <ul>
      <li v-for="production in productions.items" :key="production.id">
        {{ production.productName }} Quant. Produzida: {{ production.quantityToProduce }} - Preço unitário R$ {{ production.unitPrice }} - Preço total R$ {{ production.totalValue }}
      </li>
    </ul>
    Valor total da produção: R$ {{ productions.totalValue }}
  </div>
</template>
