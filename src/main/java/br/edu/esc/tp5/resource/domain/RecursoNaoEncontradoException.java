package br.edu.esc.tp5.resource.domain;

import br.edu.esc.tp5.shared.domain.EntityId;

public class RecursoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(EntityId id) {
        super("Recurso nao encontrado: " + id);
    }
}
