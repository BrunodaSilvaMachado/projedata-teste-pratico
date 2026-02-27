# Autoflex – Backend

## Descrição do Projeto

Autoflex é uma API REST desenvolvida em Java (Spring-Boot) que gerencia produtos, matérias-primas e sugere planos de produção com base na disponibilidade de insumos. Foi criada como prova de conceito para um teste técnico da Projedata.

## Visão geral

A aplicação expõe endpoints para CRUD de produtos e matérias-primas, e um serviço de “sugestão de produção” que calcula quantos itens podem ser fabricados de acordo com o estoque. A lógica de produção utiliza uma estratégia gulosa (greedy) para simular um algoritmo simples de otimização.

A API produz e consome JSON, está preparada para ser empacotada como WAR e implantada em um servidor Tomcat externo ou executada como aplicativo Spring Boot autônomo.

## Arquitetura

A arquitetura segue o padrão em camadas:

- **Controller** – recebe as requisições HTTP (`ProductController`, `RawMaterialController`, `ProductionController`).
- **Service** – regra de negócio (`ProductService`, `RawMaterialService`, `ProductionService`).
- **Repository** – abstração de persistência com Spring Data JPA (`ProductRepository`, `RawMaterialRepository` ...).
- **Domain** – entidades JPA (`Product`, `RawMaterial`, `ProductMaterial`).
- **DTO/Mapper** – objetos de transferência e conversão entre entidade e JSON.
- **Exception** – tratamento global de erros (`GlobalExceptionHandler`).
- **Cors** – configuração CORS para permitir chamadas do frontend.
- **Estratégia de Produção** – pacote `strategy` com implementação `GreedyProductionStrategy`.

A aplicação utiliza Spring Boot 3.x, Java 21 e banco de dados Postgres.

## Organização do repositório

``` txt
src/
├─ main/
│ ├─ java/br/com/java/autoflex/
│ │ ├─ controller/
│ │ ├─ cors/
│ │ ├─ domain/
│ │ ├─ dto/
│ │ ├─ exception/
│ │ ├─ mapper/
│ │ ├─ repository/
│ │ ├─ service/
│ │ │ └─ strategy/
│ │ ├─ AutoflexApplication.java
│ │ └─ ServletInitializer.java
│ └─ resources/
│ ├─ application.properties
│ ├─ static/
│ └─ templates/
└─ test/
└─ java/br/com/java/autoflex/
└─ ... (testes unitários por camada)
```

> pom.xml Maven com empacotamento `war`

## Decisões técnicas

### Requisitos não funcionais (RNF)

- Compatibilidade com Java 21.
- Empacotamento como WAR para Tomcat 10/11.
- Camadas bem definidas para facilitar testes unitários.
- Tratamento global de exceções.
- Uso de DTOs para desacoplar API das entidades.

### Requisitos funcionais (RF)

- CRUD de produtos.
- CRUD de matérias‑primas.
- Cálculo de sugestão de produção baseado em estoque.
- Disponibilizar API REST JSON.
- Configuração de CORS para consumo por frontend.

## Diferenciais implementados

- Estratégia de produção extraível (`ProductionAlgorithm`) que permite trocar a heurística.
- Testes unitários abrangentes em serviços e controladores.
- Mapeamento automático entre entidades e DTOs com classes mapper.
- Configuração CORS centralizada.
- Suporte a empacotamento WAR e execução standalone (SpringServletInitializer).

## Como Executar

### Pré‑requisitos

- JDK 21 instalado.
  
- Maven 3.8+ instalado.

### Compilar e gerar WAR

```powershell
cd backend\autoflex
mvn clean package
```

O artefato .war será gerado em target/autoflex.war (nome conforme pom.xml). Este WAR pode ser copiado para a pasta webapps de um Tomcat ou implantado via manager.

Executar em standalone

> mvn spring-boot:run

ou

> java -jar target/autoflex.war

A aplicação inicia na porta 8080 por padrão. Use `application.properties` para alterar configurações.

Executar testes
> mvn test

## Pendências e melhorias futuras

- Endpoints de paginação/filtragem.
- Autenticação e autorização (JWT/OAuth2).
- Implementar outras estratégias de produção (programação linear).
- Integração contínua (GitHub Actions/Maven).
- Validações mais rigorosas nos DTOs.
- Documentação da API com Swagger/OpenAPI.
