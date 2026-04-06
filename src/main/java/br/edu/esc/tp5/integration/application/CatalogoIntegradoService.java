package br.edu.esc.tp5.integration.application;

import br.edu.esc.tp5.product.domain.Produto;
import br.edu.esc.tp5.product.domain.ProdutoRepository;
import br.edu.esc.tp5.resource.domain.Recurso;
import br.edu.esc.tp5.resource.domain.RecursoRepository;
import br.edu.esc.tp5.shared.domain.EntityId;
import br.edu.esc.tp5.shared.domain.LinkedResourceIds;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CatalogoIntegradoService {

    private final ProdutoRepository produtoRepository;
    private final RecursoRepository recursoRepository;

    public CatalogoIntegradoService(ProdutoRepository produtoRepository, RecursoRepository recursoRepository) {
        if (produtoRepository == null) {
            throw new IllegalArgumentException("Repositorio de produtos e obrigatorio.");
        }
        if (recursoRepository == null) {
            throw new IllegalArgumentException("Repositorio de recursos e obrigatorio.");
        }
        this.produtoRepository = produtoRepository;
        this.recursoRepository = recursoRepository;
    }

    public void salvarProduto(ProdutoCommand command) {
        validarRecursosSelecionados(command.recursoIds());
        produtoRepository.save(Produto.criar(
                command.id(),
                command.nome(),
                command.descricao(),
                command.preco(),
                command.quantidadeEstoque(),
                command.recursoIds()
        ));
    }

    public void salvarRecurso(RecursoCommand command) {
        Recurso recurso = Recurso.criar(
                command.id(),
                command.titulo(),
                command.descricao(),
                command.situacao()
        );
        recursoRepository.save(recurso);
        if (!recurso.permiteVinculo()) {
            desvincularRecursoDosProdutos(recurso.getId());
        }
    }

    public Optional<Produto> buscarProduto(EntityId id) {
        return produtoRepository.findById(id);
    }

    public Optional<Recurso> buscarRecurso(EntityId id) {
        return recursoRepository.findById(id);
    }

    public boolean produtoExiste(EntityId id) {
        return produtoRepository.existsById(id);
    }

    public boolean recursoExiste(EntityId id) {
        return recursoRepository.existsById(id);
    }

    public void removerProduto(EntityId id) {
        produtoRepository.delete(id);
    }

    public void removerRecurso(EntityId id) {
        recursoRepository.delete(id);
        desvincularRecursoDosProdutos(id);
    }

    public List<ProdutoResumo> listarProdutos() {
        Map<EntityId, Recurso> recursosPorId = recursoRepository.findAll().stream()
                .collect(LinkedHashMap::new, (map, recurso) -> map.put(recurso.getId(), recurso), Map::putAll);
        return produtoRepository.findAll().stream()
                .map(produto -> new ProdutoResumo(
                        produto.getId().value(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco().value().toPlainString(),
                        produto.getQuantidadeEstoque().value(),
                        produto.getRecursoIds().asList().stream()
                                .map(recursosPorId::get)
                                .filter(Objects::nonNull)
                                .map(Recurso::getTitulo)
                                .toList()
                ))
                .toList();
    }

    public List<RecursoResumo> listarRecursos() {
        List<Produto> produtos = produtoRepository.findAll();
        return recursoRepository.findAll().stream()
                .map(recurso -> {
                    List<String> produtosVinculados = produtos.stream()
                            .filter(produto -> produto.getRecursoIds().contains(recurso.getId()))
                            .map(Produto::getNome)
                            .toList();
                    return new RecursoResumo(
                            recurso.getId().value(),
                            recurso.getTitulo(),
                            recurso.getDescricao(),
                            recurso.getSituacaoDescricao(),
                            produtosVinculados.size(),
                            produtosVinculados
                    );
                })
                .toList();
    }

    public List<OpcaoRecurso> listarOpcoesDeRecursos() {
        return recursoRepository.findAll().stream()
                .map(recurso -> new OpcaoRecurso(
                        recurso.getId().value(),
                        recurso.getTitulo(),
                        recurso.permiteVinculo()
                ))
                .toList();
    }

    public PainelResumo carregarPainel() {
        List<Produto> produtos = produtoRepository.findAll();
        return new PainelResumo(
                produtos.size(),
                recursoRepository.findAll().size(),
                produtos.stream()
                        .mapToInt(produto -> produto.getRecursoIds().asList().size())
                        .sum()
        );
    }

    private void validarRecursosSelecionados(LinkedResourceIds recursoIds) {
        for (EntityId recursoId : recursoIds.asList()) {
            Recurso recurso = recursoRepository.findById(recursoId)
                    .orElseThrow(() -> new IllegalArgumentException("Um ou mais recursos selecionados nao existem."));
            if (!recurso.permiteVinculo()) {
                throw new IllegalArgumentException("Um ou mais recursos selecionados estao inativos.");
            }
        }
    }

    private void desvincularRecursoDosProdutos(EntityId recursoId) {
        produtoRepository.findAll().stream()
                .filter(produto -> produto.getRecursoIds().contains(recursoId))
                .map(produto -> produto.removerVinculo(recursoId))
                .forEach(produtoRepository::save);
    }
}
