Arquitetura de Referencia do Projeto

Esta referencia usa o proprio projeto `poke-idle-3d` como base principal.
O projeto `sedoc` e a inspiracao arquitetural, traduzida para Java/Spring Boot
com nomes equivalentes sempre que fizer sentido.

## Camadas

```text
br.com.pokeidle3d
|-- api
|   |-- context
|   |-- contracts
|   |-- controllers
|   |-- filters
|   |-- handlers
|   `-- mappers
|-- application
|   |-- bus
|   |-- eventhandlers
|   |-- events
|   `-- usecases
|       `-- <caso-de-uso>
|           |-- <Caso>Command ou <Caso>Query
|           |-- <Caso>UseCase
|           `-- <Caso>Handler
|-- domain
|   |-- entities
|   |-- events
|   |-- eventstore
|   |-- exceptions
|   |-- repositories
|   `-- valueobjects
`-- infra
    |-- bus
    |-- mappers
    |-- messaging
    |-- persistence
    `-- repositories
```

## Regras de Dependencia

- `domain` nao depende de Spring, JPA, HTTP ou mensageria.
- `domain` apenas protege invariantes e registra eventos.
- `application` orquestra use cases, buses, UnitOfWork e handlers de eventos.
- `api` traduz HTTP para Commands/Queries e Responses.
- `infra` implementa JPA, mappers tecnicos, buses e publicacao/entrega.
- Controllers nao devem conter regra de negocio.
- Repositorios de dominio ficam em `domain.repositories`.
- Adapters concretos ficam em `infra.repositories`.
- Entidades JPA ficam em `infra.persistence`.
- Mappers JPA ficam em `infra.mappers`.

## CQRS

Controllers dependem de `CommandBus` e `QueryBus`.

```text
application/bus/Command.java
application/bus/Query.java
application/bus/CommandBus.java
application/bus/QueryBus.java
application/bus/CommandHandler.java
application/bus/QueryHandler.java
infra/bus/SpringCommandBus.java
infra/bus/SpringQueryBus.java
```

Cada caso de uso fica em pacote proprio:

```text
application/usecases/criarspecies
|-- CriarSpeciesCommand.java
|-- CriarSpeciesUseCase.java
`-- CriarSpeciesHandler.java
```

Commands implementam `Command<R>`.
Queries implementam `Query<R>`.
UseCases de escrita estendem `CommandHandler<C, R>`.
UseCases de leitura estendem `QueryHandler<Q, R>`.

## Event Driven Domain

O EDD deste projeto segue a ideia do `sedoc`:

```text
AggregateEventManager
-> DomainEvent
-> UnitOfWork
-> DomainNotification
-> StoreEventHandler
-> TB_EVENTO
```

### Base de Dominio

```text
domain/entities/IAggregate.java
domain/entities/AggregateEventManager.java
domain/entities/AggregateRoot.java
domain/events/DomainEvent.java
domain/valueobjects/CorrelationKey.java
```

`IAggregate` expoe:

- `events()`
- `addEvent(...)`
- `clearEvents()`

`AggregateEventManager` e o nome principal, seguindo o `sedoc`.
Aggregates que disparam eventos devem herdar dele.

`AggregateRoot` existe apenas como alias/compatibilidade para aggregates que
prefiram manter esse nome, mas o padrao novo e `AggregateEventManager`.

### DomainEvent

Todo evento deve herdar de `DomainEvent` e conter:

- `eventId`
- `occurredAt`
- `correlationKey`
- `aggregateId`
- `aggregateType`
- `userName`
- `ipRequest`
- `userAgent`
- `perfil`
- `unidade`

Eventos carregam apenas dados necessarios para consumidores.
Nao carregar entidades JPA, request HTTP, services Spring ou repositories.

Exemplo:

```text
domain/events/SpeciesCriadaEvent.java
```

### Fluxo Real

```text
HTTP request
-> CorrelationKeyFilter resolve X-Correlation-Key ou gera uma nova
-> Controller monta Command/Query
-> CommandBus ou QueryBus despacha para o Handler
-> Handler executa regra de aplicacao
-> Aggregate method registra DomainEvent via addEvent/registrarEvento
-> Repository persiste o Aggregate
-> Repository registra o Aggregate salvo no UnitOfWork
-> Handler chama saveChanges/publicarEventosPendentes
-> UnitOfWork publica depois do commit
-> PublicadorEventosDominio publica DomainNotification
-> StoreEventHandler grava TB_EVENTO
-> PublicadorEventosDominio publica o evento concreto
-> Handlers especificos reagem ao evento concreto
-> UnitOfWork limpa os eventos do Aggregate
```

## UnitOfWork

Abstracoes:

```text
application/events/UnitOfWork.java
application/events/UnidadeTrabalhoEventosDominio.java
infra/messaging/UnidadeTrabalhoEventosDominioSpring.java
```

`UnitOfWork` e o nome equivalente ao projeto `sedoc`.
`UnidadeTrabalhoEventosDominio` mantem aliases em portugues:

- `register(...)` / `registrar(...)`
- `saveChanges()` / `publicarEventosPendentes()`

A implementacao Spring deve:

1. Receber aggregates com eventos pendentes.
2. Aguardar o commit da transacao quando houver transacao ativa.
3. Enriquecer eventos com `aggregateId`, usuario, IP e user-agent.
4. Publicar via `PublicadorEventosDominio`.
5. Limpar eventos dos aggregates.
6. Limpar estado ThreadLocal.

Exemplo:

```text
infra/messaging/UnidadeTrabalhoEventosDominioSpring.java
```

## Publicacao e Event Store

Abstracoes e implementacoes:

```text
application/events/PublicadorEventosDominio.java
application/events/DomainNotification.java
infra/messaging/PublicadorEventosDominioSpring.java
application/eventhandlers/StoreEventHandler.java
domain/eventstore/entities/Event.java
domain/eventstore/repositories/EventRepository.java
infra/persistence/EventJpaEntity.java
infra/repositories/EventRepositoryJpa.java
infra/repositories/SpringDataEventJpaRepository.java
```

`PublicadorEventosDominioSpring` usa `ApplicationEventPublisher`, que e o
equivalente Spring do papel do MediatR no `sedoc`.

Para cada `DomainEvent`, ele publica:

```text
new DomainNotification(domainEvent)
domainEvent concreto
```

`DomainNotification` existe para auditoria/event store.
`StoreEventHandler` escuta `DomainNotification` e grava na tabela `TB_EVENTO`.

O evento concreto existe para reacoes especificas de negocio.
Exemplo:

```text
application/eventhandlers/SpeciesCriadaEventHandler.java
```

## Padrao Para Criar Evento em Aggregate

O evento nasce no dominio, junto da mudanca de estado:

```text
Species.criar(..., correlationKey)
-> valida dados
-> normaliza estado
-> addEvent(new SpeciesCriadaEvent(...))
```

Restauracao da persistencia nao registra evento:

```text
Species.restaurar(...)
```

## Padrao Para Command Handler

Handlers de comando devem seguir:

```text
1. Validar pre-condicoes de aplicacao.
2. Criar ou alterar aggregate.
3. Persistir via repository de dominio.
4. Solicitar publicacao ao UnitOfWork.
5. Retornar resultado.
```

Exemplos:

```text
application/usecases/criarspecies/CriarSpeciesHandler.java
infra/repositories/SpeciesRepositoryJpa.java
```

Nunca publicar eventos no controller.
Nunca publicar eventos dentro do dominio.
Evite publicar eventos diretamente no handler; use `UnitOfWork`.

## CorrelationKey

Header HTTP padrao:

```text
X-Correlation-Key
```

Fluxo:

- `CorrelationKeyFilter` le o header.
- Se o header vier vazio, gera `CorrelationKey.gerar()`.
- O valor e salvo em `CorrelationKeyContext`.
- O response recebe o header `X-Correlation-Key`.
- Controllers usam `CorrelationKeyContext` para montar Commands.
- Erros incluem `correlationKey` no body.
- Logs incluem `correlationKey`.
- Eventos recebem `correlationKey`.

Nao usar `String` crua para CorrelationKey dentro de `domain` e
`application`; use:

```text
domain/valueobjects/CorrelationKey.java
```

## Tratamento de Erro

Padrao:

```text
api/handlers/GlobalExceptionHandler.java
api/contracts/ErroResponse.java
```

Toda resposta de erro deve conter:

- `timestamp`
- `status`
- `error`
- `message`
- `path`
- `correlationKey`
- `details`

## Persistencia

O projeto usa modelo de dominio separado de modelo JPA.

```text
domain/entities/Species.java
infra/persistence/SpeciesJpaEntity.java
infra/mappers/SpeciesJpaMapper.java
infra/repositories/SpeciesRepositoryJpa.java
```

JPA/Hibernate cria as tabelas no ambiente local conforme configuracao atual do
projeto. Se migrations voltarem a ser adotadas, elas devem refletir o mesmo
modelo, incluindo `TB_EVENTO`.

## Testes Esperados Para EDD

Ao criar ou alterar eventos, cobrir pelo menos:

- aggregate registra evento;
- evento contem correlationKey;
- repository registra aggregate salvo no UnitOfWork;
- UnitOfWork publica eventos;
- UnitOfWork limpa eventos apos publicar;
- evento recebe aggregateId apos persistencia;
- `DomainNotification` gera registro em `TB_EVENTO`;
- API propaga `X-Correlation-Key`;
- erros retornam `correlationKey`.

Exemplos:

```text
src/test/java/br/com/pokeidle3d/domain/entities/SpeciesTest.java
src/test/java/br/com/pokeidle3d/application/usecases/criarspecies/CriarSpeciesHandlerTest.java
src/test/java/br/com/pokeidle3d/api/controllers/SpeciesControllerIntegrationTest.java
```

## Como Usar sedoc

Use `sedoc` para comparar estrutura e nomes:

- `AggregateEventManager`
- `DomainEvent`
- `IAggregate`
- `UnitOfWork`
- `DomainNotification`
- `StoreEventHandler`
- `Event`
- handlers concretos de eventos
- buses de Command/Query

Quando houver conflito tecnologico, traduza para Java/Spring Boot preservando
o conceito arquitetural.
