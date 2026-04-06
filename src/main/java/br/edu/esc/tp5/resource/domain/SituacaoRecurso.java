package br.edu.esc.tp5.resource.domain;

public sealed interface SituacaoRecurso permits RecursoAtivo, RecursoInativo {

    static SituacaoRecurso from(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Situacao do recurso e obrigatoria.");
        }
        return switch (raw.trim().toUpperCase()) {
            case "ATIVO" -> ativa();
            case "INATIVO" -> inativa();
            default -> throw new IllegalArgumentException("Situacao do recurso invalida.");
        };
    }

    static SituacaoRecurso ativa() {
        return RecursoAtivo.INSTANCE;
    }

    static SituacaoRecurso inativa() {
        return RecursoInativo.INSTANCE;
    }

    String nome();

    String descricao();

    boolean permiteVinculo();

    default boolean isAtivo() {
        return permiteVinculo();
    }
}
