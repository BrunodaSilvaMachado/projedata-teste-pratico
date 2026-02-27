# Autoflex Frontend

Aplicativo SPA construído em **Vue 3** com **Vite** para gerenciar produtos, matérias‑primas e sugestões de produção.

---

## 📝 Descrição do Projeto

Ferramenta interna que permite a equipe cadastrar matérias‑primas e produtos, relacionar materiais aos produtos e gerar uma sugestão de produção com base em estoque e custo. O foco é um front‑end leve e reutilizável, consumindo uma API REST (não presente neste repositório).

---

## 🔍 Visão Geral

- Listagem CRUD de matérias‑primas e produtos.
- Cada produto possui materiais exigidos com quantidades e validação para evitar duplicatas.
- Tela de "produção" mostra valor total estimado, quantidade geral e gráficos (barra e pizza) com sugestões obtidas via serviço.
- Notificações via toast e modais de confirmação.

A interface usa componentes genéricos (`BaseModal`, `BaseToast`, `BaseTable`, etc.) visando fácil reaproveitamento e consistência.

---

## 🏗 Arquitetura

- **Vue 3 (Composition API)** com componentes `<script setup>`.
- **Vite** para bundling e dev server.
- **Vitest** para testes unitários.
- **Single‑file components** separados por funcionalidade.
- **Composables** (`useToast`, `useCrudModal`, `useConfirmation`) encapsulam lógica reutilizável.
- **Serviços** em `src/services` comunicam com a API usando Axios.
- **CSS simples**: combina estilos scoped em componentes com regras globais em `style.css`.

Veja também `src/utils/format.js` para utilitários compartilhados.

---

## 📁 Organização do Repositório

```text
src/
  components/        # elementos reutilizáveis (modals, tabelas, etc.)
  layout/            # estruturas de layout (Topbar, Sidebar, BaseLayout)
  views/             # páginas principais (ProductsView, RawMaterialsView, ProductionView)
  services/          # chamadas HTTP à API
  stores/            # pinia ou similar (atualmente apenas counter demo)
  composables/       # hooks personalizados (toasts, modais, confirmação)
  utils/             # funções utilitárias (e.g. formatCurrency)
  __tests__/         # casos de teste Vitest
  App.vue
  main.js            # ponto de entrada
  style.css          # estilos globais

public/              # assets estáticos
```

---

## ⚙ Decisões Técnicas

### Requisitos Não Funcionais (RNF)

- **Leveza**: bundling via Vite com treeshaking.
- **Modularidade**: componentes/telas fáceis de manter.
- **Testabilidade**: lógica isolada em composables e serviços.
- **Acessibilidade básica**: botões via classes claras, foco mantido em forms.

### Requisitos Funcionais (RF)

- CRUD completo para matérias‑primas e produtos.
- Validação de formulário (campos obrigatórios, duplicação de material).
- Confirmação de exclusão.
- Toast de sucesso/erro.
- Visualização de produção com gráficos e cálculos de resumo.

---

## ⭐ Diferenciais Implementados

- Uso de **slots** para tornar `BaseTable` e `BaseModal` altamente configuráveis.
- Composables para abstrair fluxo de criação/edição e confirmação.
- Estratégias simples de deep‑selector (`::v-deep`) para estilização de conteúdo slotted.
- Gráficos integrados usando `vue-chartjs` com componentes reativos.
- Reset de formulário via clonagem profunda (`JSON.parse(JSON.stringify(...))`).

---

## 🚀 Como Executar

1. Instale dependências:
   ```sh
   npm install
   ```
2. Inicie servidor de desenvolvimento:
   ```sh
   npm run dev
   ```
3. Acesse `http://localhost:5173` (port padrão Vite).

### Executando testes

```sh
npm run test:unit
```

### Build para produção

```sh
npm run build
```

---

## 🧩 Pendências e Melhorias Futuras

- Adicionar autenticação/autorizações.
- Usar Pinia para centralizar estado (toasts, usuário, etc.).
- Implementar carregamento e erro nos serviços com feedback visual.
- Responsividade avançada e temas escuros/claro.
- Cobertura de testes mais abrangente (componente, integração).
- Internacionalização (`vue-i18n`).
- Documentação dos endpoints da API e mocks para desenvolvimento local.

