# Autoflex

Aplicação full stack para gerenciamento de matérias-primas e otimização de produção industrial.

O sistema permite:

- Cadastrar matérias-primas
- Cadastrar produtos com composição de materiais
- Analisar o estoque disponível
- Sugerir automaticamente quais produtos fabricar para obter o maior valor total de venda

---

## Tecnologias Utilizadas

### Backend

- Java 21
- Spring Boot 3
- JPA / Hibernate
- PostgreSQL
- Maven

### Frontend

- Vue 3 (Composition API)
- Vite
- Axios
- Vitest

---

## Estrutura do Projeto

```
autoflex/
├── autoflex-repo/ → Backend: API REST (Spring Boot)
└── autoflex-fronend-repo/ → Frontend: Aplicação SPA (Vue 3)
```


---

## Funcionalidades Implementadas

- CRUD de Matérias-Primas

- CRUD de Produtos

- Validação de materiais duplicados

- Cálculo de produção baseado em estoque

- Algoritmo guloso priorizando maior retorno financeiro

- Testes unitários no Backend

- Testes unitários básicos no Frontend

---

## Como Executar o Projeto

### Backend

Pré-requisitos:
- JDK 21
- Maven
- PostgreSQL configurado (Para teste está disponível o banco de dados H2)

```bash
cd autoflex-repo
mvn clean install
mvn spring-boot:run
```

A API será iniciada em:

> http://localhost:8080

### Frontend

Pré-requisitos:

Node.js 18+
```bash
cd autoflex-frontend-repo
npm install
npm run dev

```

Aplicação disponível em:

> http://localhost:5173

## Executar Testes

Backend

> mvn test

Frontend

> npm run test:unit

