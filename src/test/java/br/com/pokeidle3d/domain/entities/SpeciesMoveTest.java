package br.com.pokeidle3d.domain.entities;

import br.com.pokeidle3d.domain.events.MoveAddedToSpeciesMovesetEvent;
import br.com.pokeidle3d.domain.events.MoveRemovedFromSpeciesMovesetEvent;
import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import br.com.pokeidle3d.domain.valueobjects.CorrelationKey;
import br.com.pokeidle3d.domain.valueobjects.MoveLearnMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpeciesMoveTest {

    @Test
    void deveCriarAssociacaoERegistrarEventoDeAdicao() {
        CorrelationKey correlationKey = new CorrelationKey("moveset-add-key");

        SpeciesMove speciesMove = SpeciesMove.adicionar(1L, 2L, MoveLearnMethod.LEVEL_UP, 7, correlationKey);

        assertThat(speciesMove.getSpeciesId()).isEqualTo(1L);
        assertThat(speciesMove.getMoveId()).isEqualTo(2L);
        assertThat(speciesMove.getLearnMethod()).isEqualTo(MoveLearnMethod.LEVEL_UP);
        assertThat(speciesMove.getLevelLearnedAt()).isEqualTo(7);
        assertThat(speciesMove.events()).hasSize(1);
        assertThat(speciesMove.events().get(0)).isInstanceOf(MoveAddedToSpeciesMovesetEvent.class);

        MoveAddedToSpeciesMovesetEvent event = (MoveAddedToSpeciesMovesetEvent) speciesMove.events().get(0);
        assertThat(event.correlationKey()).isEqualTo(correlationKey);
        assertThat(event.speciesId()).isEqualTo(1L);
        assertThat(event.moveId()).isEqualTo(2L);
        assertThat(event.learnMethod()).isEqualTo(MoveLearnMethod.LEVEL_UP);
        assertThat(event.levelLearnedAt()).isEqualTo(7);
    }

    @Test
    void deveIgnorarLevelQuandoMetodoNaoForLevelUp() {
        SpeciesMove speciesMove = SpeciesMove.adicionar(1L, 2L, MoveLearnMethod.TM, 10, CorrelationKey.gerar());

        assertThat(speciesMove.getLevelLearnedAt()).isNull();
    }

    @Test
    void naoDeveCriarLevelUpSemLevel() {
        assertThatThrownBy(() -> SpeciesMove.adicionar(1L, 2L, MoveLearnMethod.LEVEL_UP, null, CorrelationKey.gerar()))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("LevelLearnedAt");
    }

    @Test
    void deveRegistrarEventoDeRemocao() {
        CorrelationKey correlationKey = new CorrelationKey("moveset-remove-key");
        SpeciesMove speciesMove = SpeciesMove.restaurar(10L, 1L, 2L, MoveLearnMethod.TM, null, null, null);

        speciesMove.registrarRemocao(correlationKey);

        assertThat(speciesMove.events()).hasSize(1);
        assertThat(speciesMove.events().get(0)).isInstanceOf(MoveRemovedFromSpeciesMovesetEvent.class);

        MoveRemovedFromSpeciesMovesetEvent event = (MoveRemovedFromSpeciesMovesetEvent) speciesMove.events().get(0);
        assertThat(event.correlationKey()).isEqualTo(correlationKey);
        assertThat(event.speciesId()).isEqualTo(1L);
        assertThat(event.moveId()).isEqualTo(2L);
    }
}
