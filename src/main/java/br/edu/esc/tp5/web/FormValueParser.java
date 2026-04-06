package br.edu.esc.tp5.web;

import br.edu.esc.tp5.resource.domain.SituacaoRecurso;
import br.edu.esc.tp5.shared.domain.EntityId;
import br.edu.esc.tp5.shared.domain.LinkedResourceIds;
import br.edu.esc.tp5.shared.domain.MoneyValue;
import br.edu.esc.tp5.shared.domain.StockQuantity;

import java.math.BigDecimal;
import java.util.List;

final class FormValueParser {

    private FormValueParser() {
    }

    static EntityId parseId(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("ID e obrigatorio.");
        }
        try {
            return EntityId.of(Long.parseLong(raw.trim()));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("ID invalido.");
        }
    }

    static MoneyValue parsePreco(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Preco e obrigatorio.");
        }
        try {
            return MoneyValue.of(new BigDecimal(raw.trim().replace(',', '.')));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Preco invalido.");
        }
    }

    static StockQuantity parseQuantidade(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Quantidade em estoque e obrigatoria.");
        }
        try {
            return StockQuantity.of(Integer.parseInt(raw.trim()));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Quantidade em estoque invalida.");
        }
    }

    static LinkedResourceIds parseRecursos(List<String> rawIds) {
        if (rawIds == null || rawIds.isEmpty()) {
            return LinkedResourceIds.empty();
        }
        return LinkedResourceIds.of(rawIds.stream()
                .map(FormValueParser::parseId)
                .toList());
    }

    static SituacaoRecurso parseSituacao(String raw) {
        return SituacaoRecurso.from(raw);
    }
}
