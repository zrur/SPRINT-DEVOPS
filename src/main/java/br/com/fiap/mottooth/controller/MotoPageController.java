package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.repository.MotoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/motos")
public class MotoPageController {

    private final MotoRepository motoRepository;

    public MotoPageController(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    // Lista de motos renderizada no Thymeleaf (dados vêm do Oracle via JPA)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("motos", motoRepository.findAll());
        return "motos/list"; // templates/motos/list.html
    }
}
