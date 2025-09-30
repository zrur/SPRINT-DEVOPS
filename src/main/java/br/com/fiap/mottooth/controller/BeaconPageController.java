package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.model.Beacon;
import br.com.fiap.mottooth.repository.BeaconRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/beacons")
public class BeaconPageController {

    private final BeaconRepository beaconRepository;

    public BeaconPageController(BeaconRepository beaconRepository) {
        this.beaconRepository = beaconRepository;
    }

    // Lista
    @GetMapping
    public String list(Model model) {
        model.addAttribute("beacons", beaconRepository.findAll());
        return "beacons/list";
    }

    // Novo (form)
    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("beacon", new Beacon());
        return "beacons/form";
    }

    // Novo (submit)
    @PostMapping("/novo")
    public String create(@Valid @ModelAttribute("beacon") Beacon beacon,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "beacons/form";
        // PK deve ser gerada por SEQUENCE/trigger no Oracle ou @GeneratedValue(SEQUENCE) na entidade
        beaconRepository.save(beacon);
        ra.addFlashAttribute("ok", "Beacon criado!");
        return "redirect:/beacons";
    }

    // Editar (form)
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Beacon> opt = beaconRepository.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("erro", "Beacon não encontrado.");
            return "redirect:/beacons";
        }
        model.addAttribute("beacon", opt.get());
        return "beacons/form";
    }

    // Editar (submit)
    @PostMapping("/editar/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("beacon") Beacon beacon,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "beacons/form";
        beacon.setId(id);
        beaconRepository.save(beacon);
        ra.addFlashAttribute("ok", "Beacon atualizado!");
        return "redirect:/beacons";
    }

    // Excluir
    @PostMapping("/excluir/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (beaconRepository.existsById(id)) {
            beaconRepository.deleteById(id);
            ra.addFlashAttribute("ok", "Beacon excluído.");
        } else {
            ra.addFlashAttribute("erro", "Beacon não encontrado.");
        }
        return "redirect:/beacons";
    }
}
