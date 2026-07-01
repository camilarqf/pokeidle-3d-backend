# pokeidle-3d-backend

Back-end Java com Spring Boot para o projeto Poke Idle 3D.

O projeto segue uma organizacao inspirada em DDD, CQRS, Event Driven Domain e Clean Architecture. As regras de negocio ficam concentradas no dominio, enquanto API, infraestrutura, persistencia e integracoes ficam fora do nucleo de negocio.

## Fase 0: Motor de batalha

O motor de batalha ainda esta em construcao. Ate agora, a regra pura de dominio implementada cobre o calculo de dano deterministico com os multiplicadores ja previstos para esta fase.

### Calculo de dano

Servico principal:

```text
src/main/java/br/com/pokeidle3d/domain/services/DamageCalculator.java
```

Entrada do calculo:

```text
src/main/java/br/com/pokeidle3d/domain/services/DamageCalculationInput.java
```

Campos considerados:

```java
int attackerLevel;
int movePower;
int attack;
int defense;
PokemonType moveType;
List<PokemonType> attackerTypes;
List<PokemonType> defenderTypes;
```

Formula atual:

```text
damage = baseDamage * stabMultiplier * typeEffectiveness * randomFactor
```

Ordem de calculo:

1. Calcula o dano base.
2. Calcula STAB.
3. Calcula efetividade de tipo.
4. Obtem fator aleatorio de dano.
5. Aplica os multiplicadores.
6. Arredonda para baixo.
7. Aplica dano minimo quando permitido.

Regra de imunidade:

```text
se typeEffectiveness == 0.0:
    damage = 0
senao:
    damage = max(1, floor(baseDamage * stabMultiplier * typeEffectiveness * randomFactor))
```

O dano minimo 1 nao e aplicado em imunidade.

## Regras implementadas

### Dano base

O dano base usa a formula ja existente do dominio:

```text
baseDamage = (((2 * attackerLevel / 5 + 2) * movePower * attack / defense) / 50) + 2
```

O calculo usa `BigInteger` para evitar overflow em produtos intermediarios e valida quando o resultado final excede o limite de `int`.

### STAB

Servico:

```text
src/main/java/br/com/pokeidle3d/domain/services/StabCalculator.java
```

Regra:

```text
se moveType pertence aos tipos do atacante:
    stabMultiplier = 1.5
senao:
    stabMultiplier = 1.0
```

### Efetividade de tipo

Servico:

```text
src/main/java/br/com/pokeidle3d/domain/services/TypeEffectivenessMatrix.java
```

O `DamageCalculator` nao duplica a tabela de tipos. Ele reutiliza:

```java
typeEffectivenessMatrix.getEffectiveness(input.moveType(), input.defenderTypes())
```

A efetividade considera:

- tipo do movimento;
- tipo primario do defensor;
- tipo secundario do defensor, quando existir.

Exemplos cobertos:

```text
FIRE contra GRASS = 2.0
FIRE contra GRASS/STEEL = 4.0
FIRE contra WATER = 0.5
FIRE contra WATER/ROCK = 0.25
ELECTRIC contra WATER/FLYING = 4.0
ELECTRIC contra WATER/GROUND = 0.0
NORMAL contra GHOST = 0.0
DRAGON contra FAIRY = 0.0
```

### Variacao aleatoria de dano

Abstracao:

```text
src/main/java/br/com/pokeidle3d/domain/services/DamageRandomFactorProvider.java
```

Implementacao aleatoria:

```text
src/main/java/br/com/pokeidle3d/domain/services/RandomDamageRandomFactorProvider.java
```

Implementacao fixa para testes:

```text
src/main/java/br/com/pokeidle3d/domain/services/FixedDamageRandomFactorProvider.java
```

Regra:

```text
randomFactor entre 0.85 e 1.00
```

O `RandomDamageRandomFactorProvider` gera um percentual inteiro entre 85 e 100 e converte para `BigDecimal`.

O `FixedDamageRandomFactorProvider` permite testes deterministicos e valida:

- fator nao pode ser nulo;
- fator nao pode ser menor que `0.85`;
- fator nao pode ser maior que `1.00`.

O fator aleatorio nao e obtido quando existe imunidade, pois o dano deve permanecer exatamente `0`.

## Validacoes de dominio

`DamageCalculationInput` valida:

- `attackerLevel` maior que zero;
- `movePower` maior que zero;
- `attack` maior que zero;
- `defense` maior que zero;
- `moveType` obrigatorio;
- `attackerTypes` obrigatorio;
- `attackerTypes` nao vazio;
- `attackerTypes` com no maximo 2 tipos;
- `attackerTypes` sem nulos;
- `attackerTypes` sem duplicados;
- `defenderTypes` obrigatorio;
- `defenderTypes` nao vazio;
- `defenderTypes` com no maximo 2 tipos;
- `defenderTypes` sem nulos;
- `defenderTypes` sem duplicados.

Entradas invalidas lancam `DomainValidationException`, seguindo o padrao atual do dominio.

## Estrutura relevante

```text
src/main/java/br/com/pokeidle3d/domain
|-- exceptions
|   `-- DomainValidationException.java
|-- services
|   |-- DamageCalculationInput.java
|   |-- DamageCalculator.java
|   |-- DamageRandomFactorProvider.java
|   |-- FixedDamageRandomFactorProvider.java
|   |-- RandomDamageRandomFactorProvider.java
|   |-- StabCalculator.java
|   `-- TypeEffectivenessMatrix.java
`-- valueobjects
    `-- PokemonType.java
```

Testes relacionados:

```text
src/test/java/br/com/pokeidle3d/domain/services
|-- DamageCalculatorTest.java
|-- DamageRandomFactorProviderTest.java
|-- StabCalculatorTest.java
`-- TypeEffectivenessMatrixTest.java
```

## Metodologias e decisoes tecnicas

- DDD: regras de batalha ficam no dominio, sem dependencia de Spring, JPA, banco, API ou infraestrutura.
- Clean Architecture: o dominio nao conhece camadas externas.
- CQRS: a arquitetura do projeto separa comandos e consultas na aplicacao; esta etapa nao adiciona comandos, queries ou handlers.
- Event Driven Domain: o projeto possui eventos de dominio, mas o calculo de dano atual e regra pura e nao publica eventos.
- Testabilidade: o fator aleatorio usa uma interface para permitir provider fixo em testes.
- Determinismo em testes: `FixedDamageRandomFactorProvider` garante resultados previsiveis.
- Precisao numerica: multiplicadores usam `BigDecimal`; o dano final e arredondado para baixo.

## Fora do escopo atual

Ainda nao foram implementados no calculo de dano:

- critico;
- status;
- burn;
- clima;
- habilidades;
- itens;
- motor de turnos;
- endpoints REST;
- commands;
- queries;
- handlers;
- repositories;
- persistencia;
- eventos de dominio para batalha.

## Como rodar os testes

Suite completa:

```powershell
.\mvnw.cmd test
```

Testes do motor de dano:

```powershell
.\mvnw.cmd "-Dtest=DamageCalculatorTest,DamageRandomFactorProviderTest,StabCalculatorTest,TypeEffectivenessMatrixTest" test
```
