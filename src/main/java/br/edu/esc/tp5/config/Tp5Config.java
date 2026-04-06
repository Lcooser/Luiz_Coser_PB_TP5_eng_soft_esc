package br.edu.esc.tp5.config;

import br.edu.esc.tp5.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp5.product.domain.ProdutoRepository;
import br.edu.esc.tp5.product.infrastructure.ProdutoRepositoryImpl;
import br.edu.esc.tp5.resource.domain.RecursoRepository;
import br.edu.esc.tp5.resource.infrastructure.RecursoRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Tp5Config {

    @Bean
    public ProdutoRepository produtoRepository() {
        return new ProdutoRepositoryImpl();
    }

    @Bean
    public RecursoRepository recursoRepository() {
        return new RecursoRepositoryImpl();
    }

    @Bean
    public CatalogoIntegradoService catalogoIntegradoService(
            ProdutoRepository produtoRepository,
            RecursoRepository recursoRepository
    ) {
        return new CatalogoIntegradoService(produtoRepository, recursoRepository);
    }
}
