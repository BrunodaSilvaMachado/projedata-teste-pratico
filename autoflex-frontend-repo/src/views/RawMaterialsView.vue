<script setup>
import { ref, onMounted, computed } from 'vue'
import rawMaterialService from '../services/rawMaterialService'

import BaseModal from '../components/BaseModal.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import BaseToast from '../components/BaseToast.vue'
import BaseTable from '../components/BaseTable.vue'
import HeaderActions from '../components/HeaderActions.vue'

import { useToast } from '../composables/useToast'
import { useCrudModal } from '../composables/useCrudModal'
import { useConfirmation } from '../composables/useConfirmation'

const rawMaterials = ref([])

const { toast, showToast } = useToast()

const {
  showModal,
  editing: editingMaterial,
  form,
  openCreate,
  openEdit,
} = useCrudModal({ name: '', code: '', stockQuantity: 0, unit: '' })

const { showConfirm, askDelete, confirmDelete } = useConfirmation()

const fetchMaterials = async () => {
  const response = await rawMaterialService.getAll()
  rawMaterials.value = response.data
}

onMounted(fetchMaterials)

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

const performDelete = async () => {
  const id = confirmDelete()
  if (!id) return
  await rawMaterialService.delete(id)
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

/* 🔎 Busca */
const searchQuery = ref('')

const searchedMaterials = computed(() => {
  if (!searchQuery.value) return rawMaterials.value
  return rawMaterials.value.filter((m) =>
    m.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    m.code.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})
</script>
<template>
  <div>
    <HeaderActions>
      <button class="primary" @click="openCreate">➕ Nova Matéria-Prima</button>
      <input
        v-model="searchQuery"
        type="text"
        placeholder="🔍 Buscar por nome ou código..."
        class="search-input"
      />
    </HeaderActions>

    <BaseTable>
      <template #header>
        <th>ID</th>
        <th>Nome</th>
        <th>Código</th>
        <th>Estoque</th>
        <th>Unidade</th>
        <th>Ações</th>
      </template>
      <template #body>
        <tr v-for="m in searchedMaterials" :key="m.id">
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
      </template>
    </BaseTable>

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
        <button class="cancel" @click="showModal = false">Cancelar</button>
        <button class="primary" :disabled="!isFormValid" @click="saveMaterial">Salvar</button>
      </div>
    </BaseModal>

    <ConfirmModal
      :show="showConfirm"
      message="Tem certeza que deseja excluir?"
      @cancel="showConfirm = false"
      @confirm="performDelete"
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
/* layout and table styles defined in shared components */

button.primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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

</style>
