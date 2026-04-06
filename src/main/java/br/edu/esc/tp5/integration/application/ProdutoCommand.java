package br.edu.esc.tp5.integration.application;

import br.edu.esc.tp5.shared.domain.EntityId;
import br.edu.esc.tp5.shared.domain.LinkedResourceIds;
import br.edu.esc.tp5.shared.domain.MoneyValue;
import br.edu.esc.tp5.shared.domain.StockQuantity;

public record ProdutoCommand(
        EntityId id,
        String nome,
        String descricao,
        MoneyValue preco,
        StockQuantity quantidadeEstoque,
        LinkedResourceIds recursoIds
) {
}
