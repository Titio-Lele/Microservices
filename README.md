# Microservices (Processo Seletivo)

Projeto de estudo de microservicos com Spring Boot/Spring Cloud, criado originalmente em 2020 e atualizado para uma stack moderna com Java 21.

## Resumo rapido

Estado atual do projeto:

- Java `21`
- Spring Boot `3.3.5`
- Spring Cloud `2023.0.3`
- Maven reactor com os modulos: `core`, `log`, `discovery` e `gateway`
- Persistencia do servico `log` em PostgreSQL
- Service discovery com Eureka
- API Gateway com **Spring Cloud Gateway**

## Arquitetura

### Modulos

- `core`: biblioteca compartilhada com a entidade `Course` e o `CourseRepository`
- `log`: microservico REST que expoe os cursos e acessa o banco PostgreSQL
- `discovery`: servidor Eureka para registro e descoberta de servicos
- `gateway`: gateway HTTP que recebe as chamadas externas e as encaminha para os servicos internos

### Fluxo principal

1. O cliente chama o `gateway`
2. O `gateway` resolve o servico `log` via Eureka
3. O `gateway` encaminha a requisicao para o `log`
4. O `log` usa o modulo `core` e consulta o PostgreSQL

## O que mudou na migracao

Comparado ao estado antigo do projeto:

- o projeto agora compila e empacota com **Java 21**
- a base foi migrada para **Spring Boot 3**
- o codigo JPA/validation foi atualizado de `javax.*` para `jakarta.*`
- o `gateway` deixou de usar Zuul e passou a usar **Spring Cloud Gateway**
- o build raiz passou a incluir todos os modulos no reactor

## Enderecos e portas

- Eureka dashboard: `http://localhost:8081/`
- servico `log`: `http://localhost:8082/`
- gateway: `http://localhost:8080/`

## Roteamento do gateway

O `gateway` possui a seguinte rota principal:

- entrada externa: `/gateway/log/**`
- destino interno: servico `log`
- comportamento: remove os dois primeiros segmentos do path (`StripPrefix=2`)

Exemplo:

- requisicao externa: `/gateway/log/v1/admin/course`
- requisicao encaminhada ao `log`: `/v1/admin/course`

## Como subir localmente

### 1) Pre-requisitos

- Java 21
- Maven 3.8+
- Docker e Docker Compose

### 2) Subir o PostgreSQL

O banco usado pelo servico `log` esta descrito em `log/stack.yml`.

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices/log"
docker compose -f stack.yml up -d
```

### 3) Gerar o build completo

Da raiz do projeto:

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices"
mvn clean package
```

### 4) Subir os servicos em terminais separados

#### Discovery

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices/discovery"
mvn spring-boot:run
```

#### Log

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices/log"
mvn spring-boot:run
```

#### Gateway

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices/gateway"
mvn spring-boot:run
```

## Como testar

### 1) Verificar o Eureka

Abra no navegador:

- `http://localhost:8081/`

Quando `log` e `gateway` estiverem no ar, eles devem aparecer registrados no Eureka.

### 2) Testar o endpoint diretamente no `log`

```bash
curl "http://localhost:8082/v1/admin/course"
```

### 3) Testar o mesmo endpoint via `gateway`

```bash
curl "http://localhost:8080/gateway/log/v1/admin/course"
```

## Testes Maven

### Rodar tudo pela raiz

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices"
mvn test
```

### Rodar build completo com empacotamento

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices"
mvn clean package
```

### Rodar apenas um modulo

Exemplo para `gateway`:

```bash
cd "/home/dreadlord/Documentos/Projetos/Java/Microservices"
mvn -pl gateway test
```

## Observacoes

- o build raiz foi validado com sucesso em Java 21
- os testes de `log`, `discovery` e `gateway` foram ajustados para subir o contexto com a stack atual
- o `log` usa PostgreSQL em execucao normal e H2 apenas nos testes
- ainda podem existir avisos de vulnerabilidade em analises estaticas do ecossistema Netflix/Eureka, mas o build Maven real do projeto esta funcional

