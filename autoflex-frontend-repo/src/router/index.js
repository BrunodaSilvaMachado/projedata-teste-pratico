import { createRouter, createWebHistory } from 'vue-router'
import ProductsView from '../views/ProductsView.vue'
import RawMaterialsView from '../views/RawMaterialsView.vue'
import ProductionView from '../views/ProductionView.vue'

const routes = [
  { path: '/', redirect: '/production' },
  { path: '/products', component: ProductsView },
  { path: '/raw-materials', component: RawMaterialsView },
  { path: '/production', component: ProductionView }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

export default router
