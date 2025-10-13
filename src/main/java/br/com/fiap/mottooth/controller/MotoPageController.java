package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.dto.MotoDTO;
import br.com.fiap.mottooth.model.Moto;
import br.com.fiap.mottooth.repository.ClienteRepository;
import br.com.fiap.mottooth.repository.ModeloMotoRepository;
import br.com.fiap.mottooth.repository.MotoRepository;

// >>> ADICIONE ESTES IMPORTS
import br.com.fiap.mottooth.repository.BeaconRepository;
import br.com.fiap.mottooth.repository.LocalizacaoRepository;
import br.com.fiap.mottooth.repository.MovimentacaoRepository;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/motos")
public class MotoPageController {

    private final MotoRepository motoRepository;
    private final ClienteRepository clienteRepository;
    private final ModeloMotoRepository modeloMotoRepository;

    // >>> NOVO: repositórios usados para verificar vínculos antes de excluir
    private final BeaconRepository beaconRepository;
    private final LocalizacaoRepository localizacaoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public MotoPageController(MotoRepository motoRepository,
                              ClienteRepository clienteRepository,
                              ModeloMotoRepository modeloMotoRepository,
                              // >>> NOVOS PARÂMETROS
                              BeaconRepository beaconRepository,
                              LocalizacaoRepository localizacaoRepository,
                              MovimentacaoRepository movimentacaoRepository) {
        this.motoRepository = motoRepository;
        this.clienteRepository = clienteRepository;
        this.modeloMotoRepository = modeloMotoRepository;
        // >>> ATRIBUIÇÕES NOVAS
        this.beaconRepository = beaconRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /* --------- HELPERS --------- */

    private void addClientesEModelos(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll(Sort.by("nome")));
        model.addAttribute("modelos",  modeloMotoRepository.findAll(Sort.by("nome")));
    }

    /** Normaliza placa (trim + upper) para manter consistência em toda a aplicação. */
    private static String normalizePlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase(Locale.ROOT);
    }

    private Moto toEntityForCreate(MotoDTO dto) {
        Moto m = new Moto();
        m.setPlaca(normalizePlaca(dto.getPlaca()));
        if (dto.getClienteId() != null) {
            clienteRepository.findById(dto.getClienteId()).ifPresent(m::setCliente);
        }
        if (dto.getModeloMotoId() != null) {
            modeloMotoRepository.findById(dto.getModeloMotoId()).ifPresent(m::setModeloMoto);
        }
        return m;
    }

    private MotoDTO toDTO(Moto m) {
        MotoDTO dto = new MotoDTO();
        dto.setId(m.getId());
        dto.setPlaca(m.getPlaca());
        dto.setClienteId(m.getCliente() != null ? m.getCliente().getId() : null);
        dto.setModeloMotoId(m.getModeloMoto() != null ? m.getModeloMoto().getId() : null);
        return dto;
    }

    /* --------- LISTA --------- */
    @GetMapping
    public String list(Model model) {
        List<Moto> lista = motoRepository.findAllWithRefs();
        if (lista == null) {
            lista = motoRepository.findAll(Sort.by("placa").ascending());
        }
        model.addAttribute("motos", lista);
        return "motos/list";
    }

    /* --------- NOVO (FORM) --------- */
    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("moto", new MotoDTO());
        addClientesEModelos(model);
        return "motos/form";
    }

    /* --------- NOVO (SUBMIT) --------- */
    @PostMapping("/novo")
    public String create(@Valid @ModelAttribute("moto") MotoDTO dto,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model) {

        dto.setPlaca(normalizePlaca(dto.getPlaca()));

        // valida unicidade (case-insensitive)
        if (dto.getPlaca() != null && motoRepository.existsByPlacaIgnoreCase(dto.getPlaca())) {
            br.rejectValue("placa", "placa.unique", "Placa já cadastrada.");
        }

        if (br.hasErrors()) {
            addClientesEModelos(model);
            return "motos/form";
        }

        motoRepository.save(toEntityForCreate(dto));
        ra.addFlashAttribute("ok", "Moto criada com sucesso!");
        return "redirect:/motos";
    }

    /* --------- EDITAR (FORM) --------- */
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Moto> opt = motoRepository.findByIdWithRefs(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/motos";
        }
        model.addAttribute("moto", toDTO(opt.get()));
        addClientesEModelos(model);
        return "motos/form";
    }

    /* --------- EDITAR (SUBMIT) --------- */
    @PostMapping("/editar/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("moto") MotoDTO dto,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model) {

        Moto existente = motoRepository.findById(id).orElse(null);
        if (existente == null) {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/motos";
        }

        // normaliza a placa e valida unicidade ignorando a própria moto
        String placaNorm = normalizePlaca(dto.getPlaca());
        dto.setPlaca(placaNorm);
        if (placaNorm != null && motoRepository.existsByPlacaIgnoreCaseAndIdNot(placaNorm, id)) {
            br.rejectValue("placa", "placa.unique", "Placa já cadastrada.");
        }

        if (br.hasErrors()) {
            addClientesEModelos(model);
            return "motos/form";
        }

        // ---- PATCH: atualiza somente os campos editáveis, preservando dataRegistro ----
        existente.setPlaca(placaNorm);

        if (dto.getClienteId() != null) {
            existente.setCliente(
                    clienteRepository.findById(dto.getClienteId()).orElse(null)
            );
        } else {
            existente.setCliente(null);
        }

        if (dto.getModeloMotoId() != null) {
            existente.setModeloMoto(
                    modeloMotoRepository.findById(dto.getModeloMotoId()).orElse(null)
            );
        } else {
            existente.setModeloMoto(null);
        }

        motoRepository.save(existente);

        ra.addFlashAttribute("ok", "Moto atualizada!");
        return "redirect:/motos";
    }

    /* --------- EXCLUIR --------- (atualizado para bloquear por vínculos) */
    @PostMapping("/excluir/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (!motoRepository.existsById(id)) {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/motos";
        }

        boolean temMov    = movimentacaoRepository.existsByMoto_Id(id);
        boolean temLoc    = localizacaoRepository.existsByMoto_Id(id);
        boolean temBeacon = beaconRepository.existsByMoto_Id(id);

        if (temMov || temLoc || temBeacon) {
            StringBuilder sb = new StringBuilder("Não é possível excluir a moto: existem ");
            boolean first = true;
            if (temMov)    { sb.append(first ? "" : ", ").append("movimentações"); first = false; }
            if (temLoc)    { sb.append(first ? "" : ", ").append("localizações");  first = false; }
            if (temBeacon) { sb.append(first ? "" : ", ").append("beacons"); }
            sb.append(" vinculados.");
            ra.addFlashAttribute("erro", sb.toString());
            return "redirect:/motos";
        }

        motoRepository.deleteById(id);
        ra.addFlashAttribute("ok", "Moto excluída.");
        return "redirect:/motos";
    }

    /* --------- DETALHES (opcional) --------- */
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Moto> opt = motoRepository.findByIdWithRefs(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("erro", "Moto não encontrada.");
            return "redirect:/motos";
        }
        Moto m = opt.get();
        model.addAttribute("moto", m);
        model.addAttribute("nomeCliente", m.getCliente() != null ? m.getCliente().getNome() : "—");
        model.addAttribute("modeloNome", m.getModeloMoto() != null ? m.getModeloMoto().getNome() : "—");
        model.addAttribute("fabricante", m.getModeloMoto() != null ? m.getModeloMoto().getFabricante() : "—");
        return "motos/detalhes";
    }
}
