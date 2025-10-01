package br.com.fiap.mottooth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // Página principal (home)
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Mottu Tracking");
        return "index"; // templates/index.html
    }

    // Se alguém acessar /index, redireciona para a home
    @GetMapping("/index")
    public String redirectIndex() {
        return "redirect:/";
    }

    // Exemplo de atributo para usar no HTML (botão "Voltar para Home")
    @GetMapping("/back-button")
    public String backButton(Model model) {
        model.addAttribute("voltarHome",
                "<a href='/' style='display:inline-block;padding:10px 14px;" +
                        "background:#0ea5e9;color:#001827;border-radius:10px;" +
                        "text-decoration:none;font-weight:700;'>← Voltar para Home</a>");
        return "back"; // cria um template back.html se quiser testar
    }
}
