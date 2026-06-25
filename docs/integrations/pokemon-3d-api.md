# Pokemon-3D-api

## Fonte investigada

- API server: `https://github.com/Pokemon-3D-api/api-server`
- Assets: `https://github.com/Pokemon-3D-api/assets`
- Endpoint publico documentado: `https://pokemon-3d-api.onrender.com/v1/pokemon`

## Endpoint

```text
GET https://pokemon-3d-api.onrender.com/v1/pokemon
```

O endpoint retorna um catalogo com varios Pokemon. A documentacao do projeto
mostra a estrutura:

```json
{
  "pokemon": [
    {
      "id": 1,
      "forms": [
        {
          "name": "Bulbasaur",
          "model": "https://raw.githubusercontent.com/Pokemon-3D-api/assets/main/models/opt/regular/1.glb",
          "formName": "regular"
        },
        {
          "name": "Shiny Bulbasaur",
          "model": "https://raw.githubusercontent.com/Pokemon-3D-api/assets/main/models/opt/shiny/1.glb",
          "formName": "shiny"
        }
      ]
    }
  ]
}
```

## Assets

- Formato principal: `.glb`.
- O repositorio de assets documenta pipeline de otimizacao com Draco,
  texturas redimensionadas para 1024x1024 e WebP.
- Os assets podem ser baixados diretamente pela URL `model`.
- A forma padrao deve usar `formName = regular`.

## Autenticacao e limites

- A documentacao publica nao indica autenticacao.
- A documentacao publica nao detalha rate limit.
- Para esta versao minima nao foi implementado retry, cache sofisticado ou
  circuit breaker.

## Identificacao

- Cada entrada do catalogo tem `id`, compatível com National Dex/Pokedex.
- Cada forma tem `name`, `model` e `formName`.
- Para este projeto, a importacao minima usa `Species.pokedexNumber` e a forma
  `regular`.

## Decisao de implementacao

- A aplicacao nao depende da Pokemon-3D-api em runtime.
- O importador roda apenas no profile `dev` e somente quando
  `poke-idle-3d.pokemon-3d-import.enabled=true`.
- O `.glb` e baixado para armazenamento local configuravel.
- `Species.model3dRef` passa a apontar para o caminho local salvo.
