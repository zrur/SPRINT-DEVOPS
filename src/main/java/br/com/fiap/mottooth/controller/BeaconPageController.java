package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.model.Beacon;
import br.com.fiap.mottooth.model.ModeloBeacon;
import br.com.fiap.mottooth.repository.BeaconRepository;
import br.com.fiap.mottooth.repository.ModeloBeaconRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
    private final ModeloBeaconRepository modeloBeaconRepository;

    public BeaconPageController(BeaconRepository beaconRepository,
                                ModeloBeaconRepository modeloBeaconRepository) {
        this.beaconRepository = beaconRepository;
        this.modeloBeaconRepository = modeloBeaconRepository;
    }

    // Lista com JOIN FETCH para já trazer moto/modelo
    @GetMapping
    public String list(Model model) {
        model.addAttribute("beacons", beaconRepository.findAllWithRefs());
        return "beacons/list";
    }

    // Novo (form) — carrega os modelos para o select
    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("beacon", new Beacon());
        model.addAttribute("modelos", modeloBeaconRepository.findAll(Sort.by("nome").ascending()));
        return "beacons/form";
    }

    // Novo (submit) — cria com modelo selecionado
    @PostMapping("/novo")
    public String create(@Valid @ModelAttribute("beacon") Beacon form,
                         @RequestParam("modeloBeaconId") Long modeloBeaconId,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model) {

        if (br.hasErrors()) {
            // recarrega os modelos para o select em caso de erro
            model.addAttribute("modelos", modeloBeaconRepository.findAll(Sort.by("nome").ascending()));
            return "beacons/form";
        }

        ModeloBeacon mb = modeloBeaconRepository.findById(modeloBeaconId)
                .orElseThrow(() -> new IllegalArgumentException("Modelo do beacon inválido"));

        Beacon b = new Beacon();
        b.setUuid(form.getUuid());
        b.setBateria(form.getBateria());
        b.setModeloBeacon(mb);     // <<< seta o modelo na criação
        beaconRepository.save(b);

        ra.addFlashAttribute("ok", "Beacon criado!");
        return "redirect:/beacons";
    }

    // Editar (form) — apenas exibe uuid/bateria; não precisa dos modelos aqui
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Beacon> opt = beaconRepository.findByIdWithRefs(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("erro", "Beacon não encontrado.");
            return "redirect:/beacons";
        }
        model.addAttribute("beacon", opt.get());
        return "beacons/form";
    }

    // Editar (submit) — atualiza somente uuid e bateria
    @PostMapping("/editar/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("beacon") Beacon form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) return "beacons/form";

        Beacon b = beaconRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Beacon não encontrado: " + id));

        b.setUuid(form.getUuid());
        b.setBateria(form.getBateria());
        // preserva pareamento e modelo existentes
        beaconRepository.save(b);

        ra.addFlashAttribute("ok", "Beacon atualizado!");
        return "redirect:/beacons";
    }

    // Excluir
    @PostMapping("/excluir/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            if (beaconRepository.existsById(id)) {
                beaconRepository.deleteById(id);
                ra.addFlashAttribute("ok", "Beacon excluído.");
            } else {
                ra.addFlashAttribute("erro", "Beacon não encontrado.");
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro",
                    "Não é possível excluir: há vínculos/registro de uso associados a este beacon.");
        }
        return "redirect:/beacons";
    }
}
