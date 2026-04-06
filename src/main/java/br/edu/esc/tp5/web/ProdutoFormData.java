package br.edu.esc.tp5.web;

import br.edu.esc.tp5.product.domain.Produto;
import br.edu.esc.tp5.shared.domain.EntityId;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;

public record ProdutoFormData(
        String id,
        String nome,
        String descricao,
        String preco,
        String quantidadeEstoque,
        List<String> recursoIds
) {

    public ProdutoFormData {
        id = normalizar(id);
        nome = normalizar(nome);
        descricao = normalizar(descricao);
        preco = normalizar(preco);
        quantidadeEstoque = normalizar(quantidadeEstoque);
        recursoIds = recursoIds == null
                ? List.of()
                : recursoIds.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(valor -> !valor.isEmpty())
                        .toList();
    }

    public static ProdutoFormData empty() {
        return new ProdutoFormData("", "", "", "", "", List.of());
    }

    public static ProdutoFormData from(Produto produto) {
        return new ProdutoFormData(
                produto.getId().toString(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco().value().toPlainString(),
                String.valueOf(produto.getQuantidadeEstoque().value()),
                produto.getRecursoIds().asList().stream().map(EntityId::toString).toList()
        );
    }

    public static ProdutoFormData from(MultiValueMap<String, String> parametros) {
        return new ProdutoFormData(
                primeiroValor(parametros, "id"),
                primeiroValor(parametros, "nome"),
                primeiroValor(parametros, "descricao"),
                primeiroValor(parametros, "preco"),
                primeiroValor(parametros, "quantidadeEstoque"),
                parametros.get("recursoIds")
        );
    }

    private static String primeiroValor(MultiValueMap<String, String> parametros, String chave) {
        String valor = parametros.getFirst(chave);
        return valor == null ? "" : valor;
    }

    private static String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
