package br.edu.esc.tp5.web;

import br.edu.esc.tp5.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp5.integration.application.ProdutoCommand;
import br.edu.esc.tp5.product.domain.ProdutoNaoEncontradoException;
import br.edu.esc.tp5.shared.domain.EntityId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final CatalogoIntegradoService catalogoIntegradoService;

    public ProdutoController(CatalogoIntegradoService catalogoIntegradoService) {
        this.catalogoIntegradoService = catalogoIntegradoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", catalogoIntegradoService.listarProdutos());
        return "produtos/listagem";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        preencherFormulario(model, ProdutoFormData.empty());
        return "produtos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return catalogoIntegradoService.buscarProduto(EntityId.of(id))
                .map(produto -> {
                    preencherFormulario(model, ProdutoFormData.from(produto));
                    return "produtos/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Produto nao encontrado.");
                    return "redirect:/produtos";
                });
    }

    @PostMapping
    public String salvar(
            @RequestParam MultiValueMap<String, String> parametros,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        ProdutoFormData form = ProdutoFormData.from(parametros);
        try {
            EntityId id = FormValueParser.parseId(form.id());
            boolean existia = catalogoIntegradoService.produtoExiste(id);
            catalogoIntegradoService.salvarProduto(new ProdutoCommand(
                    id,
                    form.nome(),
                    form.descricao(),
                    FormValueParser.parsePreco(form.preco()),
                    FormValueParser.parseQuantidade(form.quantidadeEstoque()),
                    FormValueParser.parseRecursos(form.recursoIds())
            ));
            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    existia ? "Produto atualizado com sucesso." : "Produto cadastrado com sucesso."
            );
            return "redirect:/produtos";
        } catch (IllegalArgumentException error) {
            model.addAttribute("erro", error.getMessage());
            preencherFormulario(model, form);
            return "produtos/formulario";
        }
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogoIntegradoService.removerProduto(EntityId.of(id));
            redirectAttributes.addFlashAttribute("sucesso", "Produto removido com sucesso.");
        } catch (ProdutoNaoEncontradoException error) {
            redirectAttributes.addFlashAttribute("erro", "Produto nao encontrado.");
        } catch (IllegalArgumentException error) {
            redirectAttributes.addFlashAttribute("erro", error.getMessage());
        }
        return "redirect:/produtos";
    }

    private void preencherFormulario(Model model, ProdutoFormData form) {
        model.addAttribute("produto", form);
        model.addAttribute("recursosDisponiveis", catalogoIntegradoService.listarOpcoesDeRecursos());
    }
}
