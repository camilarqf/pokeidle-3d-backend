Arquitetura de Referencia

Use este projeto como referencia arquitetural, nao como fonte para copiar codigo literalmente:

https://github.com/camilarqf/sedoc

O projeto de referencia e .NET/C#, portanto traduza os conceitos para Java/Spring Boot.

## Como usar a referencia

Observe principalmente:

- separacao entre Api, Application, Domain e Infrastructure
- use cases agrupados por caso de uso
- endpoints/requests separados por recurso e caso de uso
- dominio com aggregates, entities, value objects, repositories, events e exceptions
- infraestrutura de dados com repositories, mappings e contexts separados
- tratamento global de excecoes
- testes separados por camada

Nao copie nomes, regras de negocio, credenciais, endpoints ou detalhes especificos do projeto de referencia.

Se o projeto atual tiver padroes proprios, priorize o projeto atual e migre incrementalmente.

## Traducao Para Java

```text
api
├── controllers ou endpoints
├── contracts
├── handlers
└── mappers

application
├── usecases
│   └── nome-do-caso
│       ├── Command ou Query
│       └── Handler
├── services
└── events

domain
├── entities
├── valueobjects
├── events
├── repositories
└── exceptions

infra
├── persistence
│   └── entidades/modelos JPA
├── repositories
│   ├── Spring Data repositories
│   └── adapters concretos
├── mappers
├── messaging
├── config
└── integrations
```

## Fluxo Command

Controller/Endpoint -> Request contract -> Command -> Handler -> Domain -> Repository port -> Repository adapter -> JPA

## Fluxo Query

Controller/Endpoint -> Query -> Handler -> Repository port/read model -> Response contract

## Fluxo Event

Aggregate registra evento -> Application coleta eventos -> PublicadorEventosDominio publica -> Infra entrega

## Regras Praticas

- `api.contracts` substitui `api.dtos` neste projeto.
- `infra.persistence` nao deve conter Spring Data repositories; use apenas para entidades/modelos JPA.
- Spring Data repositories e adapters concretos ficam em `infra.repositories`.
- Quando houver muitos use cases, preferir `application.usecases.<caso>` com Command/Query e Handler juntos.
- Quando o dominio crescer, agrupar por contexto/recurso em vez de manter listas grandes em pacotes genericos.
