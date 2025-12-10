# 🏦 Transactions API

API REST desenvolvida em **Java 21** e **Spring Boot 4** para gestão de contas e transações financeiras.


---


## Sobre o Projeto

O objetivo é gerir transações de contas bancárias. O sistema lida automaticamente com a lógica de sinais financeiros

O sistema garante a integridade dos dados, validando a existência de contas antes de processar transações.

---

## 🛠 Stack Tecnológica

* **Linguagem:** Java 21 (Utilizando `Records` para DTOs imutáveis).
* **Framework:** Spring Boot 4.
* **Banco de Dados:** PostgreSQL (via Docker) e H2 (Testes Automatizados).
* **Build Tool:** Gradle.
* **Containerização:** Docker & Docker Compose.
* **Utils:** Acesso à dados com Spring Data JPA.

---

## 🏛 Arquitetura e Decisões

O projeto segue uma **Arquitetura em Camadas** com separação clara de responsabilidades (SRP):

1.  **Controller Layer (`web`)**: Recebe requisições HTTP.
2.  **Service Layer (`service`)**:
    * `AccountService`: Gere o ciclo de vida das contas.
    * `TransactionService`: Gere o ciclo de vida das transações, incluindo validações de saldo e existência de contas.
3.  **Repository Layer (`repository`)**: Abstração da persistência de dados (Spring Data JPA).
4.  **Domain/DTO**: Entidades JPA para persistência e Java Records para transferência de dados.
5.  **Exception Handling**: Um `RestExceptionHandler` global centraliza o tratamento de erros, retornando JSONs estruturados e amigáveis, evitando expor stacktraces.

---

## 🚀 Como Executar

A forma mais simples e recomendada é utilizando o **Docker Compose**, que sobe tanto a aplicação quanto o banco de dados PostgreSQL automaticamente.

### Pré-requisitos
* Docker e Docker Compose instalados.

### Passos

1. **Clone o repositório e entre na pasta:**
   ```bash
   git clone https://github.com/jonathantvrs/pismo.git
   cd pismo
   ```
   
2. **Construa e suba os containers:**
   ```bash
   docker-compose up --build
   ```
   
3. **Acesse a API:**
   A API estará disponível em `http://localhost:8080`.

### Endpoints Principais

- **Criar Conta**
  - `POST /accounts`
    ```bash
      curl -X POST http://localhost:8080/accounts \
        -H "Content-Type: application/json" \
        -d '{ "document_number": "12345678900" }'
    ```
- **Obter Conta**
  - `GET /accounts/{account_id}`
    ```bash
      curl -v http://localhost:8080/accounts/1
    ```
- **Criar Transação**
  - `POST /transactions`
    ```bash
      curl -X POST http://localhost:8080/transactions \
        -H "Content-Type: application/json" \
        -d '{ "account_id": 1, "operation_type_id": 1, "amount": 100.00 }'
    ```