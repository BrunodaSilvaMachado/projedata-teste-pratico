import api from './api'

export default {
  getAll() {
    return api.get('/raw-materials')
  },
  create(material) {
    return api.post('/raw-materials', material)
  },
  update(id, material) {
    return api.put(`/raw-materials/${id}`, material)
  },
  delete(id) {
    return api.delete(`/raw-materials/${id}`)
  }
}