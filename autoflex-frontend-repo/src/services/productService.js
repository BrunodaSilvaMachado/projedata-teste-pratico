import api from './api'

export default {
    getAll(){
        return api.get('/products')
    },
    create(product) {
        return api.post('/products', product)
    },
    update(id, product){
        return api.put(`/products/${id}`, product)
    },
    delete(id){
        return api.delete(`/products/${id}`)
    }
}