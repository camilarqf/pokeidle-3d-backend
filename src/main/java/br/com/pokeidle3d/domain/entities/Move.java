package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.exceptions.ValidacaoDominioException;
import br.com.pokeidle3d.domain.valueobjects.MoveCategory;
import br.com.pokeidle3d.domain.valueobjects.PokemonType;

import java.time.Instant;
import java.util.Objects;

public class Move {

    private final Long id;
    private final String name;
    private final PokemonType type;
    private final Integer power;
    private final Integer accuracy;
    private final MoveCategory category;
    private final Integer pp;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Move(
            Long id,
            String name,
            PokemonType type,
            Integer power,
            Integer accuracy,
            MoveCategory category,
            Integer pp,
            Instant createdAt,
            Instant updatedAt
    ) {
        validar(name, type, power, accuracy, category, pp);
        this.id = id;
        this.name = name.trim().toLowerCase();
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.category = category;
        this.pp = pp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Move criar(
            String name,
            PokemonType type,
            Integer power,
            Integer accuracy,
            MoveCategory category,
            Integer pp
    ) {
        return new Move(null, name, type, power, accuracy, category, pp, null, null);
    }

    public static Move restaurar(
            Long id,
            String name,
            PokemonType type,
            Integer power,
            Integer accuracy,
            MoveCategory category,
            Integer pp,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Move(id, name, type, power, accuracy, category, pp, createdAt, updatedAt);
    }

    private static void validar(
            String name,
            PokemonType type,
            Integer power,
            Integer accuracy,
            MoveCategory category,
            Integer pp
    ) {
        if (name == null || name.isBlank()) {
            throw new ValidacaoDominioException("Nome do movimento e obrigatorio");
        }
        if (type == null) {
            throw new ValidacaoDominioException("Tipo do movimento e obrigatorio");
        }
        if (category == null) {
            throw new ValidacaoDominioException("Categoria do movimento e obrigatoria");
        }
        if (pp == null || pp <= 0) {
            throw new ValidacaoDominioException("PP deve ser maior que zero");
        }
        if (power != null && power < 0) {
            throw new ValidacaoDominioException("Power nao pode ser negativo");
        }
        if (accuracy != null && (accuracy < 1 || accuracy > 100)) {
            throw new ValidacaoDominioException("Accuracy deve estar entre 1 e 100");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PokemonType getType() {
        return type;
    }

    public Integer getPower() {
        return power;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public MoveCategory getCategory() {
        return category;
    }

    public Integer getPp() {
        return pp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Move move)) {
            return false;
        }
        return id != null && Objects.equals(id, move.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
