package br.edu.esc.tp5.integration.application;

import br.edu.esc.tp5.resource.domain.SituacaoRecurso;
import br.edu.esc.tp5.shared.domain.EntityId;

public record RecursoCommand(
        EntityId id,
        String titulo,
        String descricao,
        SituacaoRecurso situacao
) {
}
