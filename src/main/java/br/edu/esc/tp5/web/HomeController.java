package br.edu.esc.tp5.web;

import br.edu.esc.tp5.integration.application.CatalogoIntegradoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CatalogoIntegradoService catalogoIntegradoService;

    @Value("${app.environment:local}")
    private String ambiente;

    @Value("${app.version:1.0.0}")
    private String versao;

    public HomeController(CatalogoIntegradoService catalogoIntegradoService) {
        this.catalogoIntegradoService = catalogoIntegradoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("painel", catalogoIntegradoService.carregarPainel());
        model.addAttribute("produtos", catalogoIntegradoService.listarProdutos().stream().limit(5).toList());
        model.addAttribute("recursos", catalogoIntegradoService.listarRecursos().stream().limit(5).toList());
        model.addAttribute("ambiente", ambiente);
        model.addAttribute("versao", versao);
        return "home";
    }
}
