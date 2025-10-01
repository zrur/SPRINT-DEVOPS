package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.model.Beacon;
import br.com.fiap.mottooth.model.Moto;
import br.com.fiap.mottooth.repository.BeaconRepository;
import br.com.fiap.mottooth.repository.MotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/flows")
public class FlowPageController {

    private final MotoRepository motoRepository;
    private final BeaconRepository beaconRepository;

    public FlowPageController(MotoRepository motoRepository, BeaconRepository beaconRepository) {
        this.motoRepository = motoRepository;
        this.beaconRepository = beaconRepository;
    }

    /* ---------- PAREAR ---------- */

    @GetMapping("/parear")
    public String parearPage(Model model) {
        List<Moto> motos = motoRepository.findAll(Sort.by("placa").ascending());
        List<Beacon> beacons = beaconRepository.findAll(Sort.by("uuid").ascending());

        model.addAttribute("motos", motos);
        model.addAttribute("beacons", beacons);
        return "flows/parear-beacon";
    }

    @PostMapping("/parear")
    @Transactional
    public String parear(@RequestParam Long motoId,
                         @RequestParam Long beaconId,
                         RedirectAttributes ra) {

        Optional<Moto> motoOpt = motoRepository.findById(motoId);
        Optional<Beacon> beaconOpt = beaconRepository.findById(beaconId);

        if (motoOpt.isEmpty() || beaconOpt.isEmpty()) {
            ra.addFlashAttribute("erro", "Moto ou Beacon não encontrado.");
            return "redirect:/flows/parear";
        }

        Moto m = motoOpt.get();
        Beacon b = beaconOpt.get();

        // validações 1-para-1
        if (b.getMoto() != null && !b.getMoto().getId().equals(m.getId())) {
            ra.addFlashAttribute("erro",
                    "Este beacon já está pareado com a moto " + b.getMoto().getPlaca() + ".");
            return "redirect:/flows/parear";
        }

        if (beaconRepository.existsByMoto_Id(m.getId())) {
            ra.addFlashAttribute("erro",
                    "A moto " + m.getPlaca() + " já possui um beacon pareado.");
            return "redirect:/flows/parear";
        }

        b.setMoto(m);
        beaconRepository.save(b);

        ra.addFlashAttribute("ok", "Beacon pareado com a moto " + m.getPlaca() + "!");
        return "redirect:/flows/parear";
    }

    /* ---------- MOVIMENTAR ---------- */

    @GetMapping("/movimentar")
    public String movimentarPage(Model model) {
        model.addAttribute("motos",
                motoRepository.findAll(Sort.by("placa").ascending()));
        return "flows/movimentacao";
    }

    @PostMapping("/movimentar")
    public String movimentar(@RequestParam Long motoId,
                             @RequestParam String tipo,
                             @RequestParam String patio,
                             RedirectAttributes ra) {

        Optional<Moto> motoOpt = motoRepository.findById(motoId);
        if (motoOpt.isEmpty()) {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/flows/movimentar";
        }

        // TODO: persistir movimentação (se houver tabela/entidade)
        ra.addFlashAttribute("ok",
                "Movimentação registrada: " + tipo + " no " + patio +
                        " para a moto " + motoOpt.get().getPlaca() + ".");
        return "redirect:/flows/movimentar";
    }
}
