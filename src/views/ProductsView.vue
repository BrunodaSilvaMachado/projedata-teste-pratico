<script setup>
import { ref, onMounted } from 'vue'
import productService from '../services/productService'

const products = ref([])

onMounted(async () => {
  try {
    const response = await productService.getAll()
    products.value = response.data
  } catch (error) {
    console.error('Erro ao buscar produtos:', error)
  }
})
</script>

<template>
  <div>
    <h1>Produtos</h1>

    <ul>
      <li v-for="product in products" :key="product.id">
        {{ product.name }} - R$ {{ product.price }}
      </li>
    </ul>
  </div>
</template>
