# Raízes do Nordeste

## Tecnologias

- Linguagem: Java 25
- Framework: Spring Boot
- Banco de Dados: PostgreSQL

## Arquitetura

O projeto utiliza uma arquitetura em camadas (hexagonal). Camadas:

- `api`
- `application`
- `domain`
- `infra`
- `config`

## Setup

1. Pré-requisitos

    - Possuir o **Java JDK 25** instalado para execução da aplicação.
    - Possuir o **Docker** instalado para execução do banco de dados.
    - Possuir o **Insomnia** para execução dos testes funcionais.

2. Clonar o repositório

   ```shell
   git clone https://github.com/gabrielluciano/raizes-nordeste.git
   ```

3. Inicie o banco de dados utilizando docker compose.

    ```shell
    cd raizes-nordeste
   
    docker compose up -d
    ```

4. Inicie a aplicação utilizando o maven a partir do terminal:

    ```shell
    cd raizes-nordeste
   
    # Linux, MacOS e WSL2
    ./mvnw spring-boot:run
   
    # Windows
    ./mvnw.cmd spring-boot:run
   
    # Caso tenha o Maven instalado
    mvn spring-boot:run
    ```

5. Seed do banco de dados

   O seed do banco é executado automaticamente ao iniciar a aplicação através da migration
   [`src/main/resources/db/migration/V2__seed.sql`](https://github.com/gabrielluciano/raizes-nordeste/blob/main/src/main/resources/db/migration/V2__seed.sql)

6. (Opcional) Encerrar os containers Docker, removendo os volumes.

   ```shell
   docker compose down -v
   ```

## Credenciais do banco de dados

A aplicação já vem configurada para conectar no banco provisionado pelo `docker compose`, não sendo
necessário nenhum ajuste manual.

As credenciais estão definidas no [`docker-compose.yml`](https://github.com/gabrielluciano/raizes-nordeste/blob/main/docker-compose.yml) e no [`src/main/resources/application.yaml`](https://github.com/gabrielluciano/raizes-nordeste/blob/main/src/main/resources/application.yaml).

Caso deseje acessar o banco para verificar os registros, use as credenciais abaixo:

| Parâmetro | Valor                                     |
|-----------|-------------------------------------------|
| Host      | `localhost`                               |
| Porta     | `5432`                                    |
| Database  | `testdb`                                  |
| Usuário   | `testuser`                                |
| Senha     | `testpass`                                |
| URL JDBC  | `jdbc:postgresql://localhost:5432/testdb` |

## Variáveis de ambiente

A aplicação utiliza as variáveis de ambiente abaixo. Todas possuem **valores padrão** definidos no
[`src/main/resources/application.yaml`](https://github.com/gabrielluciano/raizes-nordeste/blob/main/src/main/resources/application.yaml), assim **não é necessário nenhum setup de variáveis** para executar e testar o
projeto, bastando iniciar a aplicação.

| Variável         | Descrição                                                             |
|------------------|-----------------------------------------------------------------------|
| `JWT_SECRET`     | Segredo utilizado na assinatura dos tokens JWT.                       |
| `WEBHOOK_SECRET` | Segredo utilizado na autenticação do webhook do gateway de pagamento. |

Caso deseje sobrescrever os valores padrão, basta exportar as variáveis no ambiente antes de iniciar a
aplicação.

## Testes

### Testes unitários

A aplicação possui testes unitários que podem ser executados via Maven.

```shell
cd raizes-nordeste

# Linux, MacOS e WSL2

./mvnw test

# Windows

./mvnw.cmd test

# Caso tenha o Maven instalado

mvn test
```

### Testes funcionais

Uma coleção do Insomnia é fornecida para execução dos testes funcionais da API.

- Caso não possua, baixar e instalar o Insomnia em https://insomnia.rest/download.
- Acessar e baixar a coleção em [
  `testes/funcional/insomnia.yaml`](https://github.com/gabrielluciano/raizes-nordeste/blob/main/testes/funcional/insomnia.yaml)
- Importe a coleção no Insomnia.
- \[Recomendado\]: Execute toda a coleção utilizando o Runner:
  - Clique nos três pontos ao lado do nome da coleção
  - Clique em `Run Collection`
  - Clique em `Select All` para marcar todas as requests.
  - Garanta que o setup do banco foi realizado e que a aplicação foi iniciada e está executando na porta `8080`
  - No Insomnia Clique em `Run`.
  - Aguarde a finalização de cada execução. Será exibido o resultado dos casos de teste à direita.
  - Após rodar a coleção, acesse cada request individualmente para validar os corpos das requisições, parâmetros e
    respostas obtidas.

## Documentação da API: Swagger

Após iniciar a aplicação, utilize uma das duas URLs abaixo para acessar o Swagger da aplicação:

- http://localhost:8080/swagger-ui.html
- http://localhost:8080/swagger-ui/index.html

Caso deseje visualizar o JSON no formato OpenAPI 3.1.0, acesse:

- http://localhost:8080/v3/api-docs

Caso deseje visualizar o Swagger sem executar o projeto, acesse o arquivo JSON disponibilizado em
[`docs/swagger.json`](https://github.com/gabrielluciano/raizes-nordeste/blob/main/docs/swagger.json).

O conteúdo desse arquivo pode ser copiado e colado no site https://editor.swagger.io/ para visualizar o Swagger.