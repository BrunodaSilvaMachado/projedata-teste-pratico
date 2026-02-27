<script setup>
import { ref, onMounted, computed } from 'vue'
import rawMaterialService from '../services/rawMaterialService'

import BaseModal from '../components/BaseModal.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import BaseToast from '../components/BaseToast.vue'

const rawMaterials = ref([])

const showModal = ref(false)
const showConfirm = ref(false)
const editingMaterial = ref(null)
const deleteId = ref(null)

const toast = ref({
  show: false,
  message: '',
  type: 'success'
})

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 2500)
}

const emptyMaterial = {
  name: '',
  code: '',
  stockQuantity: 0,
  unit: ''
}

const form = ref({ ...emptyMaterial })

const fetchMaterials = async () => {
  const response = await rawMaterialService.getAll()
  rawMaterials.value = response.data
}

onMounted(fetchMaterials)

const openCreate = () => {
  form.value = { ...emptyMaterial }
  editingMaterial.value = null
  showModal.value = true
}

const openEdit = (material) => {
  form.value = JSON.parse(JSON.stringify(material))
  editingMaterial.value = material
  showModal.value = true
}

const saveMaterial = async () => {
  if (!isFormValid.value) {
    showToast('Preencha todos os campos corretamente.', 'error')
    return
  }

  if (editingMaterial.value) {
    await rawMaterialService.update(editingMaterial.value.id, form.value)
    showToast('Matéria-prima atualizada com sucesso!')
  } else {
    await rawMaterialService.create(form.value)
    showToast('Matéria-prima criada com sucesso!')
  }

  showModal.value = false
  await fetchMaterials()
}

const askDelete = (id) => {
  deleteId.value = id
  showConfirm.value = true
}

const confirmDelete = async () => {
  await rawMaterialService.delete(deleteId.value)
  showConfirm.value = false
  showToast('Matéria-prima removida com sucesso!')
  await fetchMaterials()
}

/* 🔎 Validação */
const isFormValid = computed(() => {
  return (
    form.value.name.trim() !== '' &&
    form.value.code.trim() !== '' &&
    form.value.unit.trim() !== '' &&
    form.value.stockQuantity >= 0
  )
})
</script>
<template>
  <div>

    <div class="header">
      <button @click="openCreate">➕ Nova Matéria-Prima</button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Código</th>
            <th>Estoque</th>
            <th>Unidade</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in rawMaterials" :key="m.id">
            <td>{{ m.id }}</td>
            <td>{{ m.name }}</td>
            <td>{{ m.code }}</td>
            <td>{{ m.stockQuantity }}</td>
            <td>{{ m.unit }}</td>
            <td>
              <button class="edit" @click="openEdit(m)">✏️</button>
              <button class="delete" @click="askDelete(m.id)">🗑️</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <BaseModal
      :show="showModal"
      :title="editingMaterial ? 'Editar Matéria-Prima' : 'Nova Matéria-Prima'"
      @close="showModal = false"
    >
      <input v-model="form.name" placeholder="Nome" />
      <input v-model="form.code" placeholder="Código" />
      <input
        type="number"
        v-model.number="form.stockQuantity"
        placeholder="Quantidade em Estoque"
      />
      <input v-model="form.unit" placeholder="Unidade (kg, un, m...)" />

      <div class="modal-actions">
        <button @click="showModal = false">Cancelar</button>
        <button
          class="primary"
          :disabled="!isFormValid"
          @click="saveMaterial"
        >
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

    <BaseToast
      :show="toast.show"
      :message="toast.message"
      :type="toast.type"
    />

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
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
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
}

button.delete {
  background: #ef4444;
  color: white;
  border: none;
  padding: 0.4rem 0.6rem;
  border-radius: 6px;
  margin-left: 0.5rem;
}

button.primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
