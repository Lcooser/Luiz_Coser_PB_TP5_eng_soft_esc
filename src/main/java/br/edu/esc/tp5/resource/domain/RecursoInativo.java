package br.edu.esc.tp5.resource.domain;

final class RecursoInativo implements SituacaoRecurso {

    static final RecursoInativo INSTANCE = new RecursoInativo();

    private RecursoInativo() {
    }

    @Override
    public String nome() {
        return "INATIVO";
    }

    @Override
    public String descricao() {
        return "Inativo";
    }

    @Override
    public boolean permiteVinculo() {
        return false;
    }

    @Override
    public String toString() {
        return nome();
    }
}
