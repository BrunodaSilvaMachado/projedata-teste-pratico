import { ref } from 'vue'

export function useConfirmation() {
  const showConfirm = ref(false)
  const targetId = ref(null)

  const askDelete = (id) => {
    targetId.value = id
    showConfirm.value = true
  }

  const confirmDelete = () => {
    const id = targetId.value
    showConfirm.value = false
    return id
  }

  return { showConfirm, askDelete, confirmDelete, targetId }
}
