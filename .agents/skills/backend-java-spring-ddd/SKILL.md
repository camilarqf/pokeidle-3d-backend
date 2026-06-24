````markdown
---
name: backend-java-spring-ddd
description: Use esta skill para criar, evoluir ou revisar backends Java com Spring Boot nas versões LTS/estáveis atuais, SQL Server, Lombok, Jakarta Validation, Actuator e Spring Web. Use quando o projeto precisar seguir DDD, CQRS, Event Driven Domain com CorrelationKey, Clean Architecture, Clean Code, Design Patterns, API REST, GlobalExceptionHandler, nomenclatura em português e separação em camadas api, application, domain e infra.
---

# Backend Java Spring DDD

Use esta skill para implementar backends Java com Spring Boot seguindo arquitetura DDD, CQRS, Event Driven Domain e Clean Architecture.

## Stack Padrão

Ao criar ou evoluir um projeto, use:

- Java LTS atual, preferindo Java 25 quando o ecossistema do projeto permitir, ou Java 21 quando houver restrição de compatibilidade.
- Spring Boot estável atual.
- Spring Web.
- Spring Data JPA.
- SQL Server Driver.
- Lombok.
- Jakarta Validation.
- Spring Boot Actuator.
- JUnit 5, Mockito e Spring Boot Test.
- Flyway para migrations, salvo se o projeto já usar Liquibase.
- MapStruct quando houver mapeamentos repetitivos entre DTOs, Commands, Queries, entidades e modelos de persistência.

Antes de escolher versões, verificar documentação oficial ou o padrão já existente no repositório.

## Estrutura

Organizar o projeto em pelo menos:

```text
br.com.empresa.projeto
├── api
├── application
├── domain
└── infra
````

Estrutura recomendada:

```text
api
├── controllers
├── dtos
├── handlers
└── mappers

application
├── commands
├── queries
├── handlers
├── usecases
├── services
└── events

domain
├── aggregates
├── entities
├── valueobjects
├── events
├── repositories
├── services
└── exceptions

infra
├── persistence
├── repositories
├── messaging
├── config
└── integrations
```

## Idioma e Nomenclatura

Usar português para classes, métodos, atributos e conceitos de domínio.

Manter termos técnicos apenas quando forem padrões reconhecidos:

* Command
* Query
* Handler
* DTO
* Repository
* Controller

Exemplos válidos:

* CriarClienteCommand
* CriarClienteCommandHandler
* BuscarClientePorIdQuery
* BuscarClientePorIdQueryHandler
* Cliente
* ClienteId
* Email
* Cpf
* NomeCompleto
* ClienteRepository
* ClienteController
* GlobalExceptionHandler

## DDD

Modelar regras de negócio no domínio.

Usar Aggregates para proteger invariantes.

Usar Value Objects em vez de strings, UUIDs, Longs ou tipos primitivos soltos quando o valor tiver significado de domínio.

Exemplos:

* ClienteId em vez de UUID
* Email em vez de String
* Cpf em vez de String
* NomeCompleto em vez de String
* CorrelationKey em vez de String

Value Objects devem validar suas próprias invariantes.

Aggregates devem expor comportamentos de negócio, não apenas getters e setters.

Entidades devem ter identidade clara.

Interfaces de repositório devem ficar no domínio ou application.

Implementações concretas devem ficar em infra.

O domínio não deve depender de Spring, JPA, mensageria ou detalhes técnicos.

## CQRS

Separar comandos de consultas.

Commands representam intenção de mudança de estado.

Queries representam leitura sem efeito colateral.

Cada Command deve ter um Handler.

Cada Query deve ter um Handler.

Não misturar fluxo de escrita e leitura no mesmo serviço quando houver regra relevante.

Commands e Queries devem carregar CorrelationKey quando fizer sentido para rastreabilidade.

Exemplo:

```java
public record CriarClienteCommand(
    NomeCompleto nome,
    Email email,
    Cpf cpf,
    CorrelationKey correlationKey
) {
}
```

## Event Driven Domain

Aggregates podem registrar Domain Events.

Domain Events devem conter:

* identificador do evento
* data e hora de ocorrência
* CorrelationKey
* dados mínimos necessários para o consumidor

Eventos devem ser publicados a partir da camada application ou infra.

O domínio deve apenas registrar eventos, não publicar em mensageria diretamente.

Criar abstrações como PublicadorEventosDominio quando necessário.

Implementações de mensageria devem ficar em infra.messaging.

## CorrelationKey

Criar Value Object CorrelationKey.

Receber o CorrelationKey via header HTTP:

```text
X-Correlation-Key
```

Se o header estiver ausente, gerar um novo valor.

Propagar CorrelationKey para:

* Commands
* Queries
* Domain Events
* logs
* respostas de erro
* chamadas externas quando existirem

Não tratar CorrelationKey como String crua fora da borda da aplicação.

## API

Controllers devem ser finos.

Controllers devem apenas:

* receber request
* validar entrada básica
* montar Command ou Query
* chamar Handler ou UseCase
* retornar response

Não expor entidades de domínio diretamente na API.

Usar DTOs para request e response.

Usar Jakarta Validation nos DTOs.

Usar status HTTP corretos.

## GlobalExceptionHandler

Criar GlobalExceptionHandler com `@RestControllerAdvice`.

Padronizar respostas de erro com:

* timestamp
* status
* erro
* mensagem
* path
* correlationKey
* camposInvalidos

Tratar pelo menos:

* exceções de domínio
* recurso não encontrado
* erro de validação
* argumento inválido
* erro inesperado

Não vazar stack trace, SQL, nomes internos de classe ou detalhes sensíveis.

## SQL Server e Infra

Usar SQL Server Driver.

Configurar datasource por variáveis de ambiente.

Não commitar credenciais.

Usar migrations versionadas.

Preferir Flyway quando não houver padrão existente.

Manter entidades JPA e detalhes de persistência em infra.

Quando o domínio ficar poluído por anotações JPA, separar modelo de domínio e modelo de persistência.

Adaptadores devem converter entre persistência e domínio.

## Lombok

Usar Lombok com moderação.

Preferir:

* @Getter
* @RequiredArgsConstructor
* @Builder

Evitar @Data em Aggregates, entidades de domínio e Value Objects.

Não criar setters públicos que permitam quebrar invariantes.

## Validação

Usar Jakarta Validation para validação sintática na API.

Usar domínio para validação semântica.

Value Objects devem impedir estado inválido.

Não duplicar regra de negócio em DTO e domínio.

## Actuator

Incluir Spring Boot Actuator.

Expor por padrão apenas endpoints seguros, como:

```properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.probes.enabled=true
```

Não expor endpoints sensíveis sem solicitação explícita.

## Clean Code e Design Patterns

Aplicar responsabilidade única.

Evitar classes grandes e métodos longos.

Preferir composição a herança.

Usar factories quando a criação de Aggregates ou Value Objects for complexa.

Usar adapters para integrações externas.

Usar ports/interfaces para dependências externas.

Usar Strategy quando houver variação real de regra de negócio.

Não criar abstrações artificiais.

Reaproveitar código por componentes pequenos e coesos.

Evitar duplicação entre handlers, mappers e validações.

## Testes

Criar testes para:

* Value Objects
* Aggregates
* Command Handlers
* Query Handlers
* Controllers quando houver comportamento HTTP relevante
* GlobalExceptionHandler
* persistência quando houver regra ou query relevante
* propagação de CorrelationKey

Usar testes unitários para domínio e application.

Usar testes de integração para infra quando necessário.

## Fluxo de Implementação

Ao implementar uma funcionalidade:

1. Identificar o Aggregate principal.
2. Modelar Value Objects antes de criar entidades.
3. Criar Commands e Queries.
4. Criar Handlers.
5. Criar eventos de domínio quando houver mudança relevante de estado.
6. Criar interfaces de repositório no domínio ou application.
7. Implementar persistência em infra.
8. Criar DTOs, mappers e controllers em api.
9. Adicionar tratamento global de erro.
10. Adicionar migrations.
11. Adicionar testes.
12. Rodar build e testes.

## Restrições

* Não colocar regra de negócio em controller.
* Não usar String para conceitos ricos de domínio.
* Não expor entidade de domínio como response da API.
* Não acoplar domínio ao Spring.
* Não usar setters públicos em Aggregates para alterar estado livremente.
* Não commitar credenciais.
* Não ignorar testes quando alterar regra de negócio.

## Referências

Quando precisar de exemplos mais concretos de estrutura, nomenclatura, fluxo Command/Query/Event, organização de pacotes ou padrões de implementação, leia:

```text
references/arquitetura.md
```

Se houver conflito entre esta referência e o projeto atual, preserve o padrão do projeto atual.

```
```
