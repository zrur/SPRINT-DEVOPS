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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("beacons", beaconRepository.findAll());
        return "beacons/list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("beacon", new Beacon());
        return "beacons/form";
    }

    @PostMapping("/novo")
    public String create(@Valid @ModelAttribute("beacon") Beacon beacon,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "beacons/form";
        beaconRepository.save(beacon);
        ra.addFlashAttribute("ok", "Beacon criado!");
        return "redirect:/beacons";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Beacon> opt = beaconRepository.findById(id);
        if (opt.isEmpty()) { ra.addFlashAttribute("erro","Beacon não encontrado."); return "redirect:/beacons"; }
        model.addAttribute("beacon", opt.get());
        return "beacons/form";
    }

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
