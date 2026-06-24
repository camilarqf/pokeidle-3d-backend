Arquitetura de Referência

Use este projeto como referência arquitetural, não como fonte para copiar código literalmente:

https://github.com/camilarqf/sedoc

## Como usar a referência

Observe principalmente:

- organização de pacotes
- separação entre api, application, domain e infra
- fluxo Command/Query/Handler
- uso de Value Objects
- tratamento global de exceções
- migrations
- testes

Não copie nomes, regras de negócio, credenciais, endpoints ou detalhes específicos do projeto de referência.

Se o projeto atual tiver padrões próprios, priorize o projeto atual.

O ponto importante: se a URL mudar, sair do ar, ficar privada ou o Codex estiver sem internet, a skill perde parte do
valor. Por isso, o ideal é colocar no arquitetura.md um resumo dos padrões importantes além da URL.

Exemplo melhor:

## Estrutura Esperada

  ```text
  api/controllers
  api/dtos
  application/commands
  application/queries
  application/handlers
  domain/aggregates
  domain/valueobjects
  domain/events
  domain/repositories
  infra/persistence
  infra/repositories
  infra/messaging
```
  ## Fluxo Command

  Controller -> RequestDTO -> Command -> CommandHandler -> Aggregate -> Repository -> DomainEvent

  ## Fluxo Query

  Controller -> Query -> QueryHandler -> Repository/ReadModel -> ResponseDTO

  ## Fluxo Event

  Aggregate registra evento -> Application coleta eventos -> PublicadorEventosDominio publica -> Infra entrega

  ## Projeto de Referência

https://github.com/camilarqf/sedoc