<script setup>
import { ref, onMounted, computed } from 'vue'
import productService from '../services/productService'
import rawMaterialService from '../services/rawMaterialService'
import { formatCurrency } from '../utils/format'

import BaseModal from '../components/BaseModal.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import BaseToast from '../components/BaseToast.vue'
import BaseTable from '../components/BaseTable.vue'
import HeaderActions from '../components/HeaderActions.vue'
import MaterialRow from '../components/MaterialRow.vue'

import { useToast } from '../composables/useToast'
import { useCrudModal } from '../composables/useCrudModal'
import { useConfirmation } from '../composables/useConfirmation'

const products = ref([])
const materials = ref([])

const { toast, showToast } = useToast()

const {
  showModal,
  editing: editingProduct,
  form,
  openCreate,
  openEdit,
} = useCrudModal({ name: '', code: '', price: 0, materials: [] })

const { showConfirm, askDelete, confirmDelete } = useConfirmation()

const fetchData = async () => {
  const [prodRes, matRes] = await Promise.all([
    productService.getAll(),
    rawMaterialService.getAll(),
  ])
  products.value = prodRes.data
  materials.value = matRes.data
}

onMounted(fetchData)

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

// deletion handled by composable; perform delete when user confirms
const performDelete = async () => {
  const id = confirmDelete()
  if (!id) return
  await productService.delete(id)
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

const hasEmptyLine = computed(() => form.value.materials.some((m) => m.rawMaterialId == null))

const canAddMaterial = computed(() => {
  if (hasEmptyLine.value) return false
  return form.value.materials.length < materials.value.length
})
</script>

<template>
  <div>
    <HeaderActions>
      <button class="primary" @click="openCreate">➕ Novo Produto</button>
    </HeaderActions>

    <BaseTable>
      <template #header>
        <th>ID</th>
        <th>Nome</th>
        <th>Código</th>
        <th>Preço</th>
        <th>Materiais</th>
        <th>Ações</th>
      </template>
      <template #body>
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
      </template>
    </BaseTable>

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

      <div v-for="(mat, index) in form.materials" :key="index">
        <MaterialRow
          :mat="mat"
          :available="getAvailableMaterials(index)"
          @remove="removeMaterial(index)"
        />
      </div>

      <button class="add-material" :disabled="!canAddMaterial" @click="addMaterial">
        ➕ Adicionar Material
      </button>

      <p v-if="!canAddMaterial" class="info-text">Todos os materiais já foram adicionados.</p>

      <div class="modal-actions">
        <button class="cancel" @click="showModal = false">Cancelar</button>
        <button class="primary" :disabled="hasDuplicateMaterials()" @click="saveProduct">
          Salvar
        </button>
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
/* moved header/table styles into shared components */

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
