package br.edu.esc.tp5.web;

import br.edu.esc.tp5.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp5.integration.application.RecursoCommand;
import br.edu.esc.tp5.resource.domain.RecursoNaoEncontradoException;
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
@RequestMapping("/recursos")
public class RecursoController {

    private final CatalogoIntegradoService catalogoIntegradoService;

    public RecursoController(CatalogoIntegradoService catalogoIntegradoService) {
        this.catalogoIntegradoService = catalogoIntegradoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("recursos", catalogoIntegradoService.listarRecursos());
        return "recursos/listagem";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("recurso", RecursoFormData.empty());
        return "recursos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return catalogoIntegradoService.buscarRecurso(EntityId.of(id))
                .map(recurso -> {
                    model.addAttribute("recurso", RecursoFormData.from(recurso));
                    return "recursos/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Recurso nao encontrado.");
                    return "redirect:/recursos";
                });
    }

    @PostMapping
    public String salvar(
            @RequestParam MultiValueMap<String, String> parametros,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        RecursoFormData form = RecursoFormData.from(parametros);
        try {
            EntityId id = FormValueParser.parseId(form.id());
            boolean existia = catalogoIntegradoService.recursoExiste(id);
            catalogoIntegradoService.salvarRecurso(new RecursoCommand(
                    id,
                    form.titulo(),
                    form.descricao(),
                    FormValueParser.parseSituacao(form.situacao())
            ));
            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    existia ? "Recurso atualizado com sucesso." : "Recurso cadastrado com sucesso."
            );
            return "redirect:/recursos";
        } catch (IllegalArgumentException error) {
            model.addAttribute("erro", error.getMessage());
            model.addAttribute("recurso", form);
            return "recursos/formulario";
        }
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogoIntegradoService.removerRecurso(EntityId.of(id));
            redirectAttributes.addFlashAttribute("sucesso", "Recurso removido com sucesso.");
        } catch (RecursoNaoEncontradoException error) {
            redirectAttributes.addFlashAttribute("erro", "Recurso nao encontrado.");
        } catch (IllegalArgumentException error) {
            redirectAttributes.addFlashAttribute("erro", error.getMessage());
        }
        return "redirect:/recursos";
    }
}
