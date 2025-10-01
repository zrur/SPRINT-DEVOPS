package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.model.Beacon;
import br.com.fiap.mottooth.model.Moto;
import br.com.fiap.mottooth.model.Movimentacao;
import br.com.fiap.mottooth.model.TipoMovimentacao;
import br.com.fiap.mottooth.repository.BeaconRepository;
import br.com.fiap.mottooth.repository.MotoRepository;
import br.com.fiap.mottooth.repository.MovimentacaoRepository;
import br.com.fiap.mottooth.repository.TipoMovimentacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/flows")
public class FlowPageController {

    private final MotoRepository motoRepository;
    private final BeaconRepository beaconRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final TipoMovimentacaoRepository tipoMovimentacaoRepository;

    public FlowPageController(MotoRepository motoRepository,
                              BeaconRepository beaconRepository,
                              MovimentacaoRepository movimentacaoRepository,
                              TipoMovimentacaoRepository tipoMovimentacaoRepository) {
        this.motoRepository = motoRepository;
        this.beaconRepository = beaconRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.tipoMovimentacaoRepository = tipoMovimentacaoRepository;
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

    /* ---------- MOVIMENTAR (ENTRADA/SAÍDA) ---------- */

    @GetMapping("/movimentar")
    public String movimentarPage(Model model) {
        model.addAttribute("motos",
                motoRepository.findAll(Sort.by("placa").ascending()));
        return "flows/movimentacao";
    }

    @PostMapping("/movimentar")
    @Transactional
    public String movimentar(@RequestParam Long motoId,
                             @RequestParam String tipo,   // ENTRADA / SAIDA
                             @RequestParam String patio,
                             RedirectAttributes ra) {

        // Valida moto
        Optional<Moto> motoOpt = motoRepository.findById(motoId);
        if (motoOpt.isEmpty()) {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/flows/movimentar";
        }
        Moto moto = motoOpt.get();

        // Normaliza e valida o tipo
        String tipoDesc = (tipo == null ? "" : tipo).trim().toUpperCase();
        if (!(tipoDesc.equals("ENTRADA") || tipoDesc.equals("SAIDA") || tipoDesc.equals("SAÍDA"))) {
            ra.addFlashAttribute("erro", "Tipo inválido. Use ENTRADA ou SAÍDA.");
            return "redirect:/flows/movimentar";
        }
        // Normaliza SAÍDA -> SAIDA (sem acento), caso venha com acento
        if (tipoDesc.equals("SAÍDA")) tipoDesc = "SAIDA";

        // Normaliza e valida o pátio
        String patioFinal = (patio == null ? "" : patio).trim();
        if (patioFinal.isEmpty()) {
            ra.addFlashAttribute("erro", "Informe o pátio.");
            return "redirect:/flows/movimentar";
        }

        // Busca (ou cria) o tipo ENTRADA/SAIDA
        TipoMovimentacao tipoEnt = tipoMovimentacaoRepository.findByDescricao(tipoDesc);
        if (tipoEnt == null) {
            tipoEnt = new TipoMovimentacao();
            tipoEnt.setDescricao(tipoDesc);
            tipoEnt = tipoMovimentacaoRepository.save(tipoEnt);
        }

        // BLOQUEIO: não permitir o mesmo movimento consecutivo para a mesma moto
        Optional<Movimentacao> ultimaOpt =
                movimentacaoRepository.findTopByMoto_IdOrderByDataMovimentacaoDesc(motoId);

        if (ultimaOpt.isPresent()
                && ultimaOpt.get().getTipoMovimentacao() != null
                && ultimaOpt.get().getTipoMovimentacao().getId().equals(tipoEnt.getId())) {
            ra.addFlashAttribute("erro",
                    "Já existe uma " + tipoDesc +
                            " registrada por último para a moto " + moto.getPlaca() +
                            ". Registre o movimento oposto antes.");
            return "redirect:/flows/movimentar";
        }

        // Persiste a movimentação
        Movimentacao mv = new Movimentacao();
        mv.setMoto(moto);
        mv.setTipoMovimentacao(tipoEnt);
        mv.setObservacao("Pátio: " + patioFinal);
        mv.setDataMovimentacao(LocalDateTime.now());
        // Se você tiver usuário logado no futuro:
        // mv.setUsuario(usuarioLogado);

        movimentacaoRepository.save(mv);

        ra.addFlashAttribute("ok",
                "Movimentação registrada: " + tipoDesc +
                        " no " + patioFinal + " (moto " + moto.getPlaca() + ").");
        return "redirect:/flows/movimentar";
    }
}
