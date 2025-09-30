package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.model.Beacon;
import br.com.fiap.mottooth.model.Moto;
import br.com.fiap.mottooth.repository.BeaconRepository;
import br.com.fiap.mottooth.repository.MotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("motos", motoRepository.findAll());
        model.addAttribute("beacons", beaconRepository.findAll());
        return "flows/parear-beacon";
    }

    @PostMapping("/parear")
    @Transactional
    public String parear(@RequestParam Long motoId,
                         @RequestParam Long beaconId,
                         Model model) {
        Optional<Moto> motoOpt = motoRepository.findById(motoId);
        Optional<Beacon> beaconOpt = beaconRepository.findById(beaconId);

        if (motoOpt.isEmpty() || beaconOpt.isEmpty()) {
            model.addAttribute("erro", "Moto ou Beacon não encontrado.");
        } else {
            Beacon b = beaconOpt.get();
            b.setMoto(motoOpt.get());
            beaconRepository.save(b);
            model.addAttribute("ok", "Beacon pareado com a moto!");
        }

        // recarrega listas para a própria página
        model.addAttribute("motos", motoRepository.findAll());
        model.addAttribute("beacons", beaconRepository.findAll());
        return "flows/parear-beacon";
    }

    /* ---------- MOVIMENTAR ---------- */

    @GetMapping("/movimentar")
    public String movimentarPage(Model model) {
        model.addAttribute("motos", motoRepository.findAll());
        return "flows/movimentacao";
    }

    @PostMapping("/movimentar")
    public String movimentar(@RequestParam Long motoId,
                             @RequestParam String tipo,
                             @RequestParam String patio,
                             Model model) {
        // TODO: Persistir registro de movimentação/localização no seu service/repository
        model.addAttribute("ok", "Movimentação registrada: " + tipo + " · " + patio);
        model.addAttribute("motos", motoRepository.findAll());
        return "flows/movimentacao";
    }
}
