<script setup>
import { ref, onMounted, computed } from 'vue'
import productService from '../services/productService'
import rawMaterialService from '../services/rawMaterialService'
import BaseModal from '../components/BaseModal.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import BaseToast from '../components/BaseToast.vue'

const products = ref([])
const materials = ref([])

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
  code: '',
  price: 0,
  materials: [],
}

const form = ref({ ...emptyProduct })

const fetchData = async () => {
  const [prodRes, matRes] = await Promise.all([
    productService.getAll(),
    rawMaterialService.getAll(),
  ])
  products.value = prodRes.data
  materials.value = matRes.data
}

onMounted(fetchData)

const openCreate = () => {
  form.value = { ...emptyProduct }
  editingProduct.value = null
  showModal.value = true
}

const openEdit = (product) => {
  form.value = JSON.parse(JSON.stringify(product))
  editingProduct.value = product
  showModal.value = true
}

const addMaterial = () => {
  form.value.materials.push({
    rawMaterialId: null,
    quantityRequired: 1,
  })
}

const removeMaterial = (index) => {
  form.value.materials.splice(index, 1)
}

const saveProduct = async () => {
  if (hasDuplicateMaterials()) {
    showToast('Não é permitido repetir materiais.', 'error')
    return
  }
  if (editingProduct.value) {
    await productService.update(editingProduct.value.id, form.value)
    showToast('Produto atualizado com sucesso!')
  } else {
    await productService.create(form.value)
    showToast('Produto criado com sucesso!')
  }

  showModal.value = false
  await fetchData()
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
  await fetchData()
}

const getMaterialName = (id) => {
  const mat = materials.value.find((m) => m.id === id)
  return mat ? mat.name : '—'
}
const hasDuplicateMaterials = () => {
  const ids = form.value.materials
    .map((m) => m.rawMaterialId) // ✅ corrigido
    .filter((id) => id !== '' && id !== -1 && id != null)

  return new Set(ids).size !== ids.length
}

const getAvailableMaterials = (currentIndex) => {
  const selectedIds = form.value.materials
    .map((m, index) => (index !== currentIndex ? m.rawMaterialId : null))
    .filter((id) => id != null)

  return materials.value.filter((material) => !selectedIds.includes(material.id))
}

const hasEmptyLine = computed(() =>
  form.value.materials.some(m => m.rawMaterialId == null)
)

const canAddMaterial = computed(() => {
  if (hasEmptyLine.value) return false
  return form.value.materials.length < materials.value.length
})
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
            <th>ID</th>
            <th>Nome</th>
            <th>Código</th>
            <th>Preço</th>
            <th>Materiais</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in products" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.name }}</td>
            <td>{{ p.code }}</td>
            <td>{{ formatCurrency(p.price) }}</td>
            <td>
              <div v-for="m in p.materials" :key="m.materialId">
                {{ getMaterialName(m.rawMaterialId) }} ({{ m.quantityRequired }})
              </div>
            </td>
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
      <input v-model="form.code" placeholder="Código" />
      <input v-model.number="form.price" type="number" placeholder="Preço" />

      <h4>Materiais</h4>

      <p v-if="hasDuplicateMaterials()" class="error-text">⚠ Material duplicado detectado.</p>

      <div v-for="(mat, index) in form.materials" :key="index" class="material-row">
        <select v-model="mat.rawMaterialId">
          <option disabled :value="null">Selecione</option>
          <option v-for="m in getAvailableMaterials(index)" :key="m.id" :value="m.id">
            {{ m.name }}
          </option>
        </select>

        <input type="number" v-model.number="mat.quantityRequired" min="1" />

        <button @click="removeMaterial(index)">❌</button>
      </div>

      <button class="add-material" :disabled="!canAddMaterial" @click="addMaterial">
        ➕ Adicionar Material
      </button>

      <p v-if="!canAddMaterial" class="info-text">Todos os materiais já foram adicionados.</p>

      <div class="modal-actions">
        <button @click="showModal = false">Cancelar</button>
        <button class="primary" :disabled="hasDuplicateMaterials()" @click="saveProduct">
          Salvar
        </button>
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

.material-row {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.material-row select,
.material-row input {
  flex: 1;
  padding: 0.4rem;
}

.add-material {
  margin: 0.5rem 0;
  background: #e0f2fe;
  border: none;
  padding: 0.5rem;
  border-radius: 6px;
  cursor: pointer;
}
.add-material:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.info-text {
  font-size: 0.85rem;
  color: #6b7280;
  margin-top: 0.3rem;
}
</style>
