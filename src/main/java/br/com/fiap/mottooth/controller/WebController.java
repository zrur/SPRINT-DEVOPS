package br.com.fiap.mottooth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/home")
    public String homePage() {
        return "home"; // templates/home.html
    }
}
