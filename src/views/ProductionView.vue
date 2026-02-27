<script setup>
import { ref, onMounted, computed } from 'vue'
import { Bar, Pie } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  ArcElement,
  CategoryScale,
  LinearScale,
} from 'chart.js'
import productionService from '../services/productionService'
import { formatCurrency } from '../utils/format'

import HeaderActions from '../components/HeaderActions.vue'
import BaseTable from '../components/BaseTable.vue'

ChartJS.register(Title, Tooltip, Legend, BarElement, ArcElement, CategoryScale, LinearScale)

const loading = ref(true)
const totalValue = ref(0)
const productions = ref([])

const fetchProduction = async () => {
  loading.value = true
  try {
    const response = await productionService.getSuggestion()
    totalValue.value = response.data.totalValue
    productions.value = response.data.items
  } finally {
    loading.value = false
  }
}

onMounted(fetchProduction)

const totalProducts = computed(() =>
  productions.value.reduce((sum, item) => sum + item.quantityToProduce, 0),
)

const distinctProducts = computed(() => productions.value.length)

const barData = computed(() => ({
  labels: productions.value.map((p) => p.productName),
  datasets: [
    {
      label: 'Quantidade Produzida',
      data: productions.value.map((p) => p.quantityToProduce),
      backgroundColor: '#3b82f6',
    },
  ],
}))

const pieData = computed(() => ({
  labels: productions.value.map((p) => p.productName),
  datasets: [
    {
      data: productions.value.map((p) => p.totalValue),
      backgroundColor: ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'],
    },
  ],
}))
</script>

<template>
  <div v-if="loading" class="skeleton-container">
    <div class="skeleton-card" v-for="n in 3" :key="n"></div>
    <div class="skeleton-chart"></div>
    <div class="skeleton-chart"></div>
  </div>

  <div v-else>
    <HeaderActions>
      <button @click="fetchProduction">🔄 Atualizar Produção</button>
    </HeaderActions>

    <!-- CARDS -->
    <div class="cards">
      <div class="card">
        <h3>💰 Valor Total</h3>
        <p>{{ formatCurrency(totalValue) }}</p>
      </div>

      <div class="card">
        <h3>📦 Total Produzido</h3>
        <p>{{ totalProducts }}</p>
      </div>

      <div class="card">
        <h3>🏭 Produtos Diferentes</h3>
        <p>{{ distinctProducts }}</p>
      </div>
    </div>

    <!-- GRÁFICO BARRAS -->
    <div class="chart-container">
      <Bar :data="barData" :options="{ responsive: true, maintainAspectRatio: false }" />
    </div>

    <!-- GRÁFICO PIZZA -->
    <div class="chart-container">
      <Pie :data="pieData" />
    </div>

    <!-- TABELA -->
    <BaseTable>
      <template #header>
        <th>Produto</th>
        <th>Quantidade</th>
        <th>Preço Unitário</th>
        <th>Valor Total</th>
      </template>
      <template #body>
        <tr v-for="item in productions" :key="item.productName">
          <td>{{ item.productName }}</td>
          <td>{{ item.quantityToProduce }}</td>
          <td>{{ formatCurrency(item.unitPrice) }}</td>
          <td>{{ formatCurrency(item.totalValue) }}</td>
        </tr>
      </template>
    </BaseTable>
  </div>
</template>

<style scoped>
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.card {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease;
}

.card:hover {
  transform: translateY(-4px);
}

.card h3 {
  font-size: 1rem;
  margin-bottom: 0.5rem;
}

.card p {
  font-size: 1.5rem;
  font-weight: bold;
}

.chart-container {
  height: 400px;
  background: white;
  padding: 1.5rem;
  margin-bottom: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.table-container {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.skeleton-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.skeleton-card,
.skeleton-chart {
  background: linear-gradient(90deg, #e5e7eb 25%, #f3f4f6 50%, #e5e7eb 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  border-radius: 12px;
}

.skeleton-card {
  height: 100px;
}

.skeleton-chart {
  height: 300px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 1rem;
}

.actions button {
  background-color: #3b82f6;
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s;
}

.actions button:hover {
  background-color: #2563eb;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 0.8rem;
  text-align: left;
}

th {
  border-bottom: 2px solid #e5e7eb;
}

@keyframes shimmer {
  0% {
    background-position: -200% 0;
  }
  100% {
    background-position: 200% 0;
  }
}
</style>
