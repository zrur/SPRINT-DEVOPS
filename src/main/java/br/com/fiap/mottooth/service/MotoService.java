package br.com.fiap.mottooth.service;

import br.com.fiap.mottooth.dto.MotoDTO;
import br.com.fiap.mottooth.model.Cliente;
import br.com.fiap.mottooth.model.ModeloMoto;
import br.com.fiap.mottooth.model.Moto;
import br.com.fiap.mottooth.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class MotoService {

    private final MotoRepository motoRepository;
    private final ClienteRepository clienteRepository;
    private final ModeloMotoRepository modeloMotoRepository;

    // repos usados para a validação de vínculos
    private final BeaconRepository beaconRepository;
    private final LocalizacaoRepository localizacaoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public MotoService(MotoRepository motoRepository,
                       ClienteRepository clienteRepository,
                       ModeloMotoRepository modeloMotoRepository,
                       BeaconRepository beaconRepository,
                       LocalizacaoRepository localizacaoRepository,
                       MovimentacaoRepository movimentacaoRepository) {
        this.motoRepository = motoRepository;
        this.clienteRepository = clienteRepository;
        this.modeloMotoRepository = modeloMotoRepository;
        this.beaconRepository = beaconRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    private static String normalizePlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase(Locale.ROOT);
    }

    /* ================== Leituras ================== */

    @Transactional(readOnly = true)
    @Cacheable(value = "motos", key = "#id")
    public MotoDTO findById(Long id) {
        Moto moto = motoRepository.findByIdWithRefs(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com ID: " + id));
        return convertToDTO(moto);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "motos", key = "'placa:' + #placa")
    public MotoDTO findByPlaca(String placa) {
        String norm = normalizePlaca(placa);
        Moto moto = motoRepository.findByPlacaIgnoreCase(norm)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com placa: " + placa));
        return convertToDTO(moto);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "motos",
            key = "'page:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<MotoDTO> findAll(Pageable pageable) {
        return motoRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "motos",
            key = "'filter:' + #placa + ':' + #clienteId + ':' + #modeloId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<MotoDTO> findByFilters(String placa, Long clienteId, Long modeloId, Pageable pageable) {
        return motoRepository.findByFilters(placa, clienteId, modeloId, pageable).map(this::convertToDTO);
    }

    /* ================== Escritas ================== */

    @Transactional
    @CacheEvict(value = "motos", allEntries = true)
    public MotoDTO save(MotoDTO motoDTO) {
        Moto moto = convertToEntity(motoDTO);
        moto.setPlaca(normalizePlaca(moto.getPlaca()));
        moto = motoRepository.save(moto);
        return convertToDTO(moto);
    }

    @Transactional
    @CacheEvict(value = "motos", allEntries = true)
    public MotoDTO update(Long id, MotoDTO motoDTO) {
        // Verifica se a moto existe
        Moto motoExistente = motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com ID: " + id));

        // Converte o DTO em entidade
        Moto moto = convertToEntity(motoDTO);

        // 🔥 Preserva a data de registro anterior (evita ORA-01407)
        moto.setDataRegistro(motoExistente.getDataRegistro());

        // Define ID e normaliza placa
        moto.setId(id);
        moto.setPlaca(normalizePlaca(moto.getPlaca()));

        // Salva a atualização
        moto = motoRepository.save(moto);
        return convertToDTO(moto);
    }

    @Transactional
    @CacheEvict(value = "motos", allEntries = true)
    public void delete(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new EntityNotFoundException("Moto não encontrada com ID: " + id);
        }

        // ======== Valida vínculos antes de excluir (Opção A) ========
        boolean temMov = movimentacaoRepository.existsByMoto_Id(id);
        boolean temLoc = localizacaoRepository.existsByMoto_Id(id);
        boolean temBeacon = beaconRepository.existsByMoto_Id(id);

        if (temMov || temLoc || temBeacon) {
            StringBuilder sb = new StringBuilder("Não é possível excluir a moto: existem ");
            boolean first = true;
            if (temMov) {
                sb.append(first ? "" : ", ").append("movimentações");
                first = false;
            }
            if (temLoc) {
                sb.append(first ? "" : ", ").append("localizações");
                first = false;
            }
            if (temBeacon) {
                sb.append(first ? "" : ", ").append("beacons");
            }
            sb.append(" vinculados.");
            throw new IllegalStateException(sb.toString());
        }

        try {
            motoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Não foi possível excluir: existem registros dependentes vinculados à moto."
            );
        }
    }

    /* ================== mapeamentos ================== */

    private MotoDTO convertToDTO(Moto moto) {
        MotoDTO dto = new MotoDTO();
        dto.setId(moto.getId());
        dto.setPlaca(moto.getPlaca());

        if (moto.getCliente() != null) {
            dto.setClienteId(moto.getCliente().getId());
            dto.setNomeCliente(moto.getCliente().getNome());
        }

        if (moto.getModeloMoto() != null) {
            dto.setModeloMotoId(moto.getModeloMoto().getId());
            dto.setModeloNome(moto.getModeloMoto().getNome());
            dto.setFabricante(moto.getModeloMoto().getFabricante());
        }

        return dto;
    }

    private Moto convertToEntity(MotoDTO dto) {
        Moto moto = new Moto();
        moto.setId(dto.getId());
        moto.setPlaca(dto.getPlaca());

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.getReferenceById(dto.getClienteId());
            moto.setCliente(cliente);
        } else {
            moto.setCliente(null);
        }

        if (dto.getModeloMotoId() != null) {
            ModeloMoto modeloMoto = modeloMotoRepository.getReferenceById(dto.getModeloMotoId());
            moto.setModeloMoto(modeloMoto);
        } else {
            moto.setModeloMoto(null);
        }

        return moto;
    }
}
