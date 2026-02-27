import { describe, it, expect } from 'vitest'
import { useToast } from '../composables/useToast'

describe('useToast', () => {
  it('initially not shown and can show then hide', () => {
    const { toast, showToast } = useToast()
    expect(toast.value.show).toBe(false)
    showToast('hello', 'error')
    expect(toast.value.show).toBe(true)
    expect(toast.value.message).toBe('hello')
    expect(toast.value.type).toBe('error')
  })
})
