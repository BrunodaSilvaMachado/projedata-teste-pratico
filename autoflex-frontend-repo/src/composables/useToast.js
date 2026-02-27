import { ref } from 'vue'

export function useToast() {
  const toast = ref({ show: false, message: '', type: 'success' })

  const showToast = (message, type = 'success') => {
    toast.value = { show: true, message, type }
    setTimeout(() => (toast.value.show = false), 2500)
  }

  return { toast, showToast }
}
