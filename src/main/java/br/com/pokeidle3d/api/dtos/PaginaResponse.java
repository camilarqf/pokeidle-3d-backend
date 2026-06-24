package br.com.pokeidle3d.api.dtos;

import java.util.List;

public record PaginaResponse<T>(
        List<T> itens,
        long totalItens,
        int totalPaginas,
        int pagina,
        int tamanho
) {
}
