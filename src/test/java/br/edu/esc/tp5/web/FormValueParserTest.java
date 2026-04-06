package br.edu.esc.tp5.web;

import br.edu.esc.tp5.resource.domain.SituacaoRecurso;
import br.edu.esc.tp5.shared.domain.EntityId;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormValueParserTest {

    @Test
    void criaProdutoFormNormalizadoAPartirDosParametros() {
        MultiValueMap<String, String> parametros = new LinkedMultiValueMap<>();
        parametros.add("id", " 10 ");
        parametros.add("nome", " Kit ");
        parametros.add("descricao", " Catalogo ");
        parametros.add("preco", " 9.90 ");
        parametros.add("quantidadeEstoque", " 3 ");
        parametros.add("recursoIds", " 1 ");
        parametros.add("recursoIds", "");

        ProdutoFormData form = ProdutoFormData.from(parametros);

        assertEquals("10", form.id());
        assertEquals("Kit", form.nome());
        assertEquals("Catalogo", form.descricao());
        assertEquals("9.90", form.preco());
        assertEquals("3", form.quantidadeEstoque());
        assertEquals(List.of("1"), form.recursoIds());
    }

    @Test
    void criaRecursoFormComSituacaoPadrao() {
        RecursoFormData form = RecursoFormData.empty();

        assertEquals("ATIVO", form.situacao());
    }

    @Test
    void converteIdValido() {
        assertEquals(EntityId.of(7L), FormValueParser.parseId("7"));
    }

    @Test
    void converteRecursosSelecionados() {
        assertEquals(
                List.of(EntityId.of(1L), EntityId.of(2L)),
                FormValueParser.parseRecursos(List.of("1", "2")).asList()
        );
    }

    @Test
    void converteSituacaoInativa() {
        assertEquals(SituacaoRecurso.inativa(), FormValueParser.parseSituacao("INATIVO"));
    }

    @Test
    void rejeitaPrecoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> FormValueParser.parsePreco("abc"));
    }

    @Test
    void devolveColecaoVaziaQuandoNaoHaRecursos() {
        assertTrue(FormValueParser.parseRecursos(List.of()).isEmpty());
    }
}
