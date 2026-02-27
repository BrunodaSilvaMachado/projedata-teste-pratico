<script setup>
import { ref, onMounted } from 'vue'
import productService from '../services/productService'
import BaseModal from '../components/BaseModal.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import BaseToast from '../components/BaseToast.vue'

const products = ref([])
const showModal = ref(false)
const editingProduct = ref(null)
const showConfirm = ref(false)
const deleteId = ref(null)

const toast = ref({
  show: false,
  message: '',
  type: 'success',
})

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => (toast.value.show = false), 2500)
}

const emptyProduct = {
  name: '',
  price: 0,
}

const form = ref({ ...emptyProduct })

const fetchProducts = async () => {
  const response = await productService.getAll()
  products.value = response.data
}

onMounted(fetchProducts)

const openCreate = () => {
  form.value = { ...emptyProduct }
  editingProduct.value = null
  showModal.value = true
}

const openEdit = (product) => {
  form.value = { ...product }
  editingProduct.value = product
  showModal.value = true
}

const saveProduct = async () => {
  if (editingProduct.value) {
    await productService.update(editingProduct.value.id, form.value)
  } else {
    await productService.create(form.value)
  }

  showModal.value = false
  await fetchProducts()
}

const formatCurrency = (value) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value)
}

const askDelete = (id) => {
  deleteId.value = id
  showConfirm.value = true
}

const confirmDelete = async () => {
  await productService.delete(deleteId.value)
  showConfirm.value = false
  showToast('Produto removido com sucesso!')
  await fetchProducts()
}
</script>

<template>
  <div>
    <div class="header">
      <button @click="openCreate">➕ Novo Produto</button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Código</th>
            <th>Preço</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in products" :key="p.id">
            <td>{{ p.name }}</td>
            <td>{{ p.code }}</td>
            <td>{{ formatCurrency(p.price) }}</td>
            <td>
              <button class="edit" @click="openEdit(p)">✏️</button>
              <button class="delete" @click="askDelete(p.id)">🗑️</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- MODAL -->
    <BaseModal
      :show="showModal"
      :title="editingProduct ? 'Editar Produto' : 'Novo Produto'"
      @close="showModal = false"
    >
      <input v-model="form.name" placeholder="Nome" />
      <input v-model.number="form.price" type="number" placeholder="Preço" />

      <div class="modal-actions">
        <button @click="showModal = false">Cancelar</button>
        <button class="primary" @click="saveProduct">Salvar</button>
      </div>
    </BaseModal>

    <ConfirmModal
      :show="showConfirm"
      message="Tem certeza que deseja excluir?"
      @cancel="showConfirm = false"
      @confirm="confirmDelete"
    />

    <BaseToast :show="toast.show" :message="toast.message" :type="toast.type" />
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 1rem;
}

.header button {
  background: #3b82f6;
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  cursor: pointer;
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 0.8rem;
}

th {
  border-bottom: 2px solid #e5e7eb;
}

button.edit {
  background: #facc15;
  border: none;
  padding: 0.4rem 0.6rem;
  border-radius: 6px;
  cursor: pointer;
}

button.delete {
  background: #ef4444;
  color: white;
  border: none;
  padding: 0.4rem 0.6rem;
  border-radius: 6px;
  margin-left: 0.5rem;
  cursor: pointer;
}

/* MODAL */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  width: 400px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.modal input {
  padding: 0.6rem;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.modal-actions .primary {
  background: #3b82f6;
  color: white;
}
</style>
