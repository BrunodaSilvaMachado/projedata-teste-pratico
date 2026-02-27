import { ref } from 'vue'

export function useCrudModal(emptyEntity) {
  const showModal = ref(false)
  const editing = ref(null)
  const form = ref({ ...emptyEntity })

  const openCreate = () => {
    form.value = { ...emptyEntity }
    editing.value = null
    showModal.value = true
  }

  const openEdit = (entity) => {
    form.value = JSON.parse(JSON.stringify(entity))
    editing.value = entity
    showModal.value = true
  }

  return { showModal, editing, form, openCreate, openEdit }
}
