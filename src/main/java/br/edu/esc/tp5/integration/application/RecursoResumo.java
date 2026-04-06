package br.edu.esc.tp5.integration.application;

import java.util.List;

public record RecursoResumo(
        Long id,
        String titulo,
        String descricao,
        String situacao,
        int totalProdutos,
        List<String> produtos
) {
}
