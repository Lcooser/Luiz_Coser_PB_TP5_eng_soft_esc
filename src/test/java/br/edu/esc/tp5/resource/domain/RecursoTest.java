package br.edu.esc.tp5.resource.domain;

import br.edu.esc.tp5.shared.domain.EntityId;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecursoTest {

    @Test
    void rejeitaTituloVazio() {
        assertThrows(IllegalArgumentException.class, () -> Recurso.criar(
                EntityId.of(1L),
                " ",
                "Descricao",
                SituacaoRecurso.ativa()
        ));
    }

    @Test
    void marcaRecursoAtivoCorretamente() {
        Recurso recurso = Recurso.criar(EntityId.of(1L), "Manual", "Descricao", SituacaoRecurso.ativa());
        assertTrue(recurso.isAtivo());
        assertEquals("Ativo", recurso.getSituacaoDescricao());
    }

    @Test
    void rejeitaDescricaoMuitoLonga() {
        String descricao = "x".repeat(2001);

        assertThrows(IllegalArgumentException.class, () -> Recurso.criar(
                EntityId.of(2L),
                "Manual",
                descricao,
                SituacaoRecurso.ativa()
        ));
    }

    @Test
    void igualdadeConsideraSomenteId() {
        Recurso primeiro = Recurso.criar(EntityId.of(3L), "Manual A", "Descricao", SituacaoRecurso.ativa());
        Recurso segundo = Recurso.criar(EntityId.of(3L), "Manual B", "Descricao", SituacaoRecurso.inativa());

        assertEquals(primeiro, segundo);
        assertEquals(Objects.hash(EntityId.of(3L)), primeiro.hashCode());
    }
}
