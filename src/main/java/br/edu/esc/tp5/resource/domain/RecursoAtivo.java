package br.edu.esc.tp5.resource.domain;

final class RecursoAtivo implements SituacaoRecurso {

    static final RecursoAtivo INSTANCE = new RecursoAtivo();

    private RecursoAtivo() {
    }

    @Override
    public String nome() {
        return "ATIVO";
    }

    @Override
    public String descricao() {
        return "Ativo";
    }

    @Override
    public boolean permiteVinculo() {
        return true;
    }

    @Override
    public String toString() {
        return nome();
    }
}
