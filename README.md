# API de Funcionários

Uma API RESTful desenvolvida com Spring Boot para gerenciamento de funcionários e cargos. Esta aplicação fornece endpoints completos para operações CRUD (Create, Read, Update, Delete) em um banco de dados PostgreSQL.

## 🚀 Funcionalidades

- **Gerenciamento de Cargos:** Operações completas de CRUD para cadastro de cargos
- **Gerenciamento de Funcionários:** Operações completas de CRUD para cadastro de funcionários
- **Validação de Dados:** Validação automática de campos obrigatórios, formato de CPF, e-mail e telefone
- **Tratamento de Exceções:** Tratamento global de erros com mensagens claras
- **Nome Único:** Garantia de unicidade de nome no cadastro de funcionários e cargos
- **CPF Único:** Garantia de unicidade de CPF no cadastro de funcionários
- **E-mail Único:** Garantia de unicidade de e-mail no cadastro de funcionários
- **Relacionamento:** Cada funcionário deve estar associado a um cargo existente
- **Proteção de Integridade:** Não permite exclusão de cargos que possuem funcionários associados
- **DTOs:** Separação clara entre entidades de banco e objetos de transferência de dados
- **Testes Unitários:** Cobertura de testes para a camada de serviço com JUnit e Mockito
- **Paginação:** Listagem com suporte a paginação

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 4.0.7
- Spring Data JPA
- Spring Web MVC
- Spring Validation
- PostgreSQL
- Lombok
- Maven
- JUnit 5
- Mockito

## 📋 Pré-requisitos

- Java 17 ou superior instalado
- Maven 3.6+ instalado
- PostgreSQL 12+ instalado e rodando
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 🔧 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone <repository-url>
cd API-de-Funcionarios
```

### 2. Configure o Banco de Dados

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE api_funcionarios;
```

### 3. Configure as Credenciais

Edite o arquivo `src/main/resources/application.properties` ou configure as variáveis de ambiente:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/api_funcionarios
spring.datasource.username=postgres
spring.datasource.password=sua_senha
```

Ou usando variáveis de ambiente:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/api_funcionarios
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
```

### 4. Compile o Projeto

```bash
./mvnw clean install
```

Ou usando Maven diretamente:

```bash
mvn clean install
```

## ▶️ Executando a Aplicação

### Via Maven

```bash
./mvnw spring-boot:run
```

### Via JAR

```bash
java -jar target/API-de-Funcionarios-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: http://localhost:8080

## 📡 Endpoints da API

### Cargos

#### Criar Cargo
```http
POST /cargo
Content-Type: application/json

{
  "nome": "Desenvolvedor",
  "descricao": "Desenvolvedor de Software"
}
```

Response: `201 Created`
```json
{
  "id": 1,
  "nome": "Desenvolvedor",
  "descricao": "Desenvolvedor de Software"
}
```

#### Listar Todos os Cargos
```http
GET /cargo?page=0&size=10
```

Response: `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "nome": "Desenvolvedor",
      "descricao": "Desenvolvedor de Software"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### Buscar Cargos por Nome
```http
GET /cargo?nome=Desenvolvedor&page=0&size=10
```

Response: `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "nome": "Desenvolvedor",
      "descricao": "Desenvolvedor de Software"
    }
  ]
}
```

#### Buscar Cargo por ID
```http
GET /cargo/{id}
```

Response: `200 OK`
```json
{
  "id": 1,
  "nome": "Desenvolvedor",
  "descricao": "Desenvolvedor de Software"
}
```

#### Atualizar Cargo
```http
PUT /cargo/{id}
Content-Type: application/json

{
  "nome": "Desenvolvedor Senior",
  "descricao": "Desenvolvedor Senior de Software"
}
```

Response: `200 OK`
```json
{
  "id": 1,
  "nome": "Desenvolvedor Senior",
  "descricao": "Desenvolvedor Senior de Software"
}
```

#### Deletar Cargo
```http
DELETE /cargo/{id}
```

Response: `204 No Content`

### Funcionários

#### Criar Funcionário
```http
POST /funcionario
Content-Type: application/json

{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "email": "joao.silva@email.com",
  "telefone": "11999999999",
  "salario": 5000.00,
  "dataAdmissao": "2024-01-01",
  "cargoId": 1
}
```

Response: `201 Created`
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "email": "joao.silva@email.com",
  "telefone": "11999999999",
  "salario": 5000.00,
  "dataAdmissao": "2024-01-01",
  "cargo": "Desenvolvedor"
}
```

#### Listar Todos os Funcionários
```http
GET /funcionario?page=0&size=10
```

Response: `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "nome": "João Silva",
      "cpf": "123.456.789-00",
      "email": "joao.silva@email.com",
      "telefone": "11999999999",
      "salario": 5000.00,
      "dataAdmissao": "2024-01-01",
      "cargo": "Desenvolvedor"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### Buscar Funcionários por Nome
```http
GET /funcionario?nome=João&page=0&size=10
```

Response: `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "nome": "João Silva",
      "cpf": "123.456.789-00",
      "email": "joao.silva@email.com",
      "telefone": "11999999999",
      "salario": 5000.00,
      "dataAdmissao": "2024-01-01",
      "cargo": "Desenvolvedor"
    }
  ]
}
```

#### Buscar Funcionário por ID
```http
GET /funcionario/{id}
```

Response: `200 OK`
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "email": "joao.silva@email.com",
  "telefone": "11999999999",
  "salario": 5000.00,
  "dataAdmissao": "2024-01-01",
  "cargo": "Desenvolvedor"
}
```

#### Buscar Funcionário por CPF
```http
GET /funcionario/cpf?cpf=123.456.789-00
```

Response: `200 OK`
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "email": "joao.silva@email.com",
  "telefone": "11999999999",
  "salario": 5000.00,
  "dataAdmissao": "2024-01-01",
  "cargo": "Desenvolvedor"
}
```

#### Atualizar Funcionário
```http
PUT /funcionario/{id}
Content-Type: application/json

{
  "nome": "Maria Silva",
  "cpf": "987.654.321-00",
  "email": "maria.silva@email.com",
  "telefone": "11888888888",
  "salario": 6000.00,
  "dataAdmissao": "2024-02-01",
  "cargoId": 1
}
```

Response: `200 OK`
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "cpf": "987.654.321-00",
  "email": "maria.silva@email.com",
  "telefone": "11888888888",
  "salario": 6000.00,
  "dataAdmissao": "2024-02-01",
  "cargo": "Desenvolvedor"
}
```

#### Deletar Funcionário
```http
DELETE /funcionario/{id}
```

Response: `204 No Content`

## 🧪 Testes

Para executar os testes unitários:

```bash
./mvnw test
```

Ou usando Maven:

```bash
mvn test
```

## 📄 Estrutura do Projeto

```
API-de-Funcionarios/
├── src/
│   ├── main/
│   │   ├── java/com/API_de_Funcionarios/
│   │   │   ├── controller/     # Controladores REST
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── entity/         # Entidades JPA
│   │   │   ├── exception/      # Tratamento de exceções
│   │   │   ├── repository/     # Repositórios JPA
│   │   │   └── service/        # Lógica de negócio
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/API_de_Funcionarios/
│           └── service/        # Testes unitários
├── pom.xml
└── README.md
```

## 🔍 Validações

### Cargo
- **nome:** Obrigatório, entre 3 e 100 caracteres
- **nome:** Deve ser único (case-insensitive)
- **descricao:** Opcional, máximo 200 caracteres

### Funcionário
- **nome:** Obrigatório, entre 3 e 100 caracteres
- **nome:** Deve ser único (case-insensitive)
- **cpf:** Obrigatório, deve ser único
- **email:** Obrigatório, formato válido de e-mail
- **email:** Deve ser único
- **telefone:** Obrigatório, entre 10 e 15 caracteres
- **salario:** Obrigatório, deve ser maior que zero
- **dataAdmissao:** Obrigatório, não pode ser futura
- **cargoId:** Obrigatório, deve referenciar um cargo existente

## ⚠️ Tratamento de Erros

A API retorna códigos de status HTTP apropriados:

- **201 Created:** Recurso criado com sucesso
- **200 OK:** Operação realizada com sucesso
- **204 No Content:** Recurso deletado com sucesso
- **400 Bad Request:** Dados inválidos
- **404 Not Found:** Recurso não encontrado
- **409 Conflict:** Nome, CPF ou e-mail já cadastrado, ou cargo em uso
- **500 Internal Server Error:** Erro interno no servidor

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 👤 Autor

**Flavio Alexandre Ribeiro**

Desenvolvido como parte do projeto de API de Funcionários.

## 📧 Contato

- GitHub: https://github.com/FlavioAlexandreRibeiro
- LinkedIn: https://www.linkedin.com/in/flavioalexandreribeiro1986/?isSelfProfile=true

Para dúvidas ou sugestões, abra uma issue no repositório.
