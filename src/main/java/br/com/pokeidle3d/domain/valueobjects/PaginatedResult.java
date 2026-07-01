package br.com.pokeidle3d.domain.valueobjects;

import java.util.List;

public record PaginatedResult<T>(
        List<T> itens,
        long totalItens,
        int totalPaginas,
        int pagina,
        int tamanho
) {
}
