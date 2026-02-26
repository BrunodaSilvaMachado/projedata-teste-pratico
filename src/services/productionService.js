import api from './api'

export default {
  getSuggestion() {
    return api.get('/production/suggestion')
  }
}