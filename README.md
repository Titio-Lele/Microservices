# Microservices (Processo Seletivo)

Projeto de estudo com Spring Boot e Spring Cloud, organizado como um multi-módulo Maven.

## Checklist rápido

- [x] Java `21`
- [x] Spring Boot `3.3.5`
- [x] Spring Cloud `2023.0.3`
- [x] Módulos `core`, `log`, `discovery` e `gateway`
- [x] PostgreSQL para o serviço `log`
- [x] Eureka para service discovery
- [x] Spring Cloud Gateway para roteamento externo

## Visão geral

### Módulos

- `core`: biblioteca compartilhada com a entidade `Course` e o `CourseRepository`
- `discovery`: servidor Eureka
- `log`: microserviço REST com persistência em PostgreSQL
- `gateway`: API Gateway que encaminha chamadas para o serviço `log`

### Fluxo principal

1. O cliente chama o `gateway`
2. O `gateway` resolve o serviço `log` via Eureka
3. O `gateway` encaminha a requisição para o `log`
4. O `log` usa o módulo `core` e consulta o PostgreSQL

## Portas e endpoints

- Eureka dashboard: `http://localhost:8081/`
- Serviço `log`: `http://localhost:8082/`
- Gateway: `http://localhost:8080/`

### Rota configurada no gateway

- entrada externa: `/gateway/log/**`
- destino interno: `lb://log`
- filtro aplicado: `StripPrefix=2`

Exemplo:

- entrada: `/gateway/log/v1/admin/course`
- destino real no `log`: `/v1/admin/course`

## Pré-requisitos

- Java 21
- Maven 3.8+ ou uso do wrapper `./mvnw`
- Docker com `docker compose`

## Banco de dados

O PostgreSQL usado pelo serviço `log` está em `log/stack.yml`.

Da raiz do projeto:

```zsh
docker compose -f log/stack.yml up -d
```

O serviço `log` usa, por padrão:

- banco: `devdojo`
- usuário: `postgres`
- senha: `postgres`
- porta: `5432`

As migrations do serviço `log` ficam em `log/src/main/resources/db/migration` e são aplicadas automaticamente na subida da aplicação.

> Observação: versões recentes do Docker Compose exibem um aviso informando que o campo `version` do arquivo YAML é obsoleto. É apenas um aviso e não impede a subida do container.

## Build do projeto

Na raiz do repositório:

```zsh
./mvnw clean package
```

Se preferir usar o Maven instalado no sistema:

```zsh
mvn clean package
```

## Ordem recomendada para subir localmente

Suba os serviços nesta ordem:

1. `discovery`
2. `log`
3. `gateway`

## Como executar

### Opção 1: executar a partir da raiz com `-pl`

#### Discovery

```zsh
./mvnw -pl discovery spring-boot:run
```

#### Log

```zsh
./mvnw -pl log -am spring-boot:run
```

#### Gateway

```zsh
./mvnw -pl gateway spring-boot:run
```

Sem usar `-pl`, a alternativa nativa do Maven é apontar diretamente para o `pom` do módulo:

```zsh
./mvnw -f gateway/pom.xml spring-boot:run
```

### Opção 2: executar dentro de cada módulo

#### Discovery

```zsh
cd discovery
../mvnw spring-boot:run
```

#### Log

```zsh
cd log
../mvnw spring-boot:run
```

#### Gateway

```zsh
cd gateway
../mvnw spring-boot:run
```

## Como testar

### Verificar registro no Eureka

Abra:

- `http://localhost:8081/`

Quando `log` e `gateway` estiverem no ar, ambos devem aparecer registrados.

### Testar o endpoint diretamente no `log`

```zsh
curl "http://localhost:8082/v1/admin/course"
```

### Testar o endpoint via `gateway`

```zsh
curl "http://localhost:8080/gateway/log/v1/admin/course"
```

## Testes Maven

### Rodar todos os testes

```zsh
./mvnw test
```

### Rodar build completo

```zsh
./mvnw clean package
```

### Rodar testes de um módulo específico

Exemplo para `gateway`:

```zsh
./mvnw -pl gateway test
```

## Troubleshooting

### 1. `gateway` falha com `Unable to find a suitable main class`

Se o erro mencionar o projeto `processo-seletivo`, o comando foi executado no `pom.xml` raiz, que é apenas agregador (`packaging=pom`) e não possui classe `main`.

Use um dos comandos abaixo:

```zsh
./mvnw -pl gateway spring-boot:run
```

ou:

```zsh
cd gateway
../mvnw spring-boot:run
```

ou, a partir da raiz, apontando para o `pom.xml` do módulo:

```zsh
./mvnw -f gateway/pom.xml spring-boot:run
```

### 2. `log` falha com `Not a managed type: class br.com.alexandredev.core.model.Course`

Esse problema estava relacionado ao escaneamento de entidades JPA no módulo `log`.

Se você estiver com classes antigas em cache, execute:

```zsh
./mvnw clean compile -pl core,log -am
```

e então suba novamente o serviço:

```zsh
./mvnw -pl log -am spring-boot:run
```

### 3. Docker falha com erro de socket `docker.sock`

Se aparecer algo como:

- `failed to connect to the docker API`
- `dial unix /run/user/1000/docker.sock: connect: no such file or directory`

então o daemon Docker não está em execução.

Em ambientes com Docker rootless, valide o serviço do usuário:

```zsh
systemctl --user status docker
systemctl --user start docker
```

Depois tente novamente:

```zsh
docker compose -f log/stack.yml up -d
```

## Observações

- o módulo `core` é biblioteca compartilhada e não deve ser executado isoladamente como aplicação Spring Boot
- o `log` usa PostgreSQL em execução normal e H2 apenas nos testes
- o `gateway` depende do Eureka para resolver `lb://log`
- o `discovery` sobe na porta `8081` com o nome de aplicação `registry`
- a forma mais simples de subir o `gateway` sem `-pl` é executar dentro de `gateway/` ou usar `-f gateway/pom.xml`

