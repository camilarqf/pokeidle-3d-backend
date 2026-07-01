package br.com.pokeidle3d.domain.services;

import br.com.pokeidle3d.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DamageRandomFactorProviderTest {

    @Test
    void providerAleatorioNuncaRetornaValorMenorQueMinimo() {
        RandomDamageRandomFactorProvider provider = new RandomDamageRandomFactorProvider();

        for (int i = 0; i < 200; i++) {
            assertThat(provider.nextFactor().compareTo(DamageRandomFactorProvider.MIN_FACTOR))
                    .isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    void providerAleatorioNuncaRetornaValorMaiorQueMaximo() {
        RandomDamageRandomFactorProvider provider = new RandomDamageRandomFactorProvider();

        for (int i = 0; i < 200; i++) {
            assertThat(provider.nextFactor().compareTo(DamageRandomFactorProvider.MAX_FACTOR))
                    .isLessThanOrEqualTo(0);
        }
    }

    @Test
    void providerAleatorioRetornaValorValidoEmMultiplasExecucoes() {
        RandomDamageRandomFactorProvider provider = new RandomDamageRandomFactorProvider();

        for (int i = 0; i < 200; i++) {
            BigDecimal factor = provider.nextFactor();

            assertThat(factor)
                    .isBetween(DamageRandomFactorProvider.MIN_FACTOR, DamageRandomFactorProvider.MAX_FACTOR);
        }
    }

    @Test
    void providerFixoRetornaValorConfigurado() {
        FixedDamageRandomFactorProvider provider = new FixedDamageRandomFactorProvider(BigDecimal.valueOf(0.93));

        BigDecimal factor = provider.nextFactor();

        assertThat(factor).isEqualByComparingTo(BigDecimal.valueOf(0.93));
    }

    @Test
    void providerFixoRejeitaValorNulo() {
        assertThatThrownBy(() -> new FixedDamageRandomFactorProvider(null))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("obrigatorio");
    }

    @Test
    void providerFixoRejeitaValorMenorQueMinimo() {
        assertThatThrownBy(() -> new FixedDamageRandomFactorProvider(BigDecimal.valueOf(0.84)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("menor que 0.85");
    }

    @Test
    void providerFixoRejeitaValorMaiorQueMaximo() {
        assertThatThrownBy(() -> new FixedDamageRandomFactorProvider(BigDecimal.valueOf(1.01)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("maior que 1.00");
    }
}
