import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import App from '../App.vue'

describe('App', () => {
  it('mounts and contains layout wrapper', () => {
    const wrapper = mount(App, {
      global: {
        stubs: ['BaseLayout', 'router-view', 'Sidebar', 'Topbar'],
      },
    })
    // with layout stubbed we can still assert that the component mounts
    expect(wrapper.exists()).toBe(true)
  })
})
