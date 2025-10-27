package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UsuarioService usuarioService;

    public RegisterController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
            @RequestParam String email,
            @RequestParam String nome,
            @RequestParam String senha,
            RedirectAttributes ra
    ) {
        try {
            usuarioService.salvarNovoUsuario(email, nome, senha);
            ra.addFlashAttribute("msgOk", "Conta criada! Faça login.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("msgErr", ex.getMessage());
            return "redirect:/register";
        } catch (Exception ex) {
            ra.addFlashAttribute("msgErr", "Erro ao cadastrar o usuário.");
            return "redirect:/register";
        }
    }
}
