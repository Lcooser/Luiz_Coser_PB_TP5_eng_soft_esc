package br.edu.esc.tp5.web;

import br.edu.esc.tp5.resource.domain.Recurso;
import br.edu.esc.tp5.resource.domain.SituacaoRecurso;
import org.springframework.util.MultiValueMap;

public record RecursoFormData(
        String id,
        String titulo,
        String descricao,
        String situacao
) {

    public RecursoFormData {
        id = normalizar(id);
        titulo = normalizar(titulo);
        descricao = normalizar(descricao);
        situacao = normalizar(situacao);
    }

    public static RecursoFormData empty() {
        return new RecursoFormData("", "", "", SituacaoRecurso.ativa().nome());
    }

    public static RecursoFormData from(Recurso recurso) {
        return new RecursoFormData(
                recurso.getId().toString(),
                recurso.getTitulo(),
                recurso.getDescricao(),
                recurso.getSituacaoNome()
        );
    }

    public static RecursoFormData from(MultiValueMap<String, String> parametros) {
        return new RecursoFormData(
                primeiroValor(parametros, "id"),
                primeiroValor(parametros, "titulo"),
                primeiroValor(parametros, "descricao"),
                primeiroValor(parametros, "situacao")
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
