package br.edu.esc.tp5.resource.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SituacaoRecursoTest {

    @Test
    void resolveEstadoAtivoPorFactory() {
        SituacaoRecurso situacao = SituacaoRecurso.from("ativo");

        assertSame(SituacaoRecurso.ativa(), situacao);
        assertTrue(situacao.permiteVinculo());
    }

    @Test
    void resolveEstadoInativoPorFactory() {
        SituacaoRecurso situacao = SituacaoRecurso.from("INATIVO");

        assertSame(SituacaoRecurso.inativa(), situacao);
        assertFalse(situacao.permiteVinculo());
    }

    @Test
    void rejeitaEstadoDesconhecido() {
        assertThrows(IllegalArgumentException.class, () -> SituacaoRecurso.from("rascunho"));
    }
}
