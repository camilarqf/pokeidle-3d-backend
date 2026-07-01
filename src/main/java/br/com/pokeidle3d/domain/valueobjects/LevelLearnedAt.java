package br.com.pokeidle3d.domain.valueobjects;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;

public record LevelLearnedAt(Integer value) {

    public LevelLearnedAt {
        if (value == null || value <= 0) {
            throw new DomainValidationException("LevelLearnedAt deve ser maior que zero");
        }
    }

    public static LevelLearnedAt obrigatorioParaLevelUp(MoveLearnMethod learnMethod, Integer value) {
        if (learnMethod == MoveLearnMethod.LEVEL_UP) {
            return new LevelLearnedAt(value);
        }
        return null;
    }
}
