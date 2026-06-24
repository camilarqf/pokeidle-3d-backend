package br.com.pokeidle3d.api.contracts;

import java.util.List;

public record PaginaResponse<T>(
        List<T> itens,
        long totalItens,
        int totalPaginas,
        int pagina,
        int tamanho
) {
}
