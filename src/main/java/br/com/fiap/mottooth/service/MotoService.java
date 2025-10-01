package br.com.fiap.mottooth.service;

import br.com.fiap.mottooth.dto.MotoDTO;
import br.com.fiap.mottooth.model.Cliente;
import br.com.fiap.mottooth.model.ModeloMoto;
import br.com.fiap.mottooth.model.Moto;
import br.com.fiap.mottooth.repository.ClienteRepository;
import br.com.fiap.mottooth.repository.ModeloMotoRepository;
import br.com.fiap.mottooth.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    public MotoService(MotoRepository motoRepository,
                       ClienteRepository clienteRepository,
                       ModeloMotoRepository modeloMotoRepository) {
        this.motoRepository = motoRepository;
        this.clienteRepository = clienteRepository;
        this.modeloMotoRepository = modeloMotoRepository;
    }

    private static String normalizePlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase(Locale.ROOT);
    }

    @Cacheable(value = "motos", key = "#id")
    public MotoDTO findById(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com ID: " + id));
        return convertToDTO(moto);
    }

    @Cacheable(value = "motos", key = "'placa:' + #placa")
    public MotoDTO findByPlaca(String placa) {
        String norm = normalizePlaca(placa);
        Moto moto = motoRepository.findByPlacaIgnoreCase(norm)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com placa: " + placa));
        return convertToDTO(moto);
    }

    @Cacheable(value = "motos",
            key = "'page:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<MotoDTO> findAll(Pageable pageable) {
        return motoRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Cacheable(value = "motos",
            key = "'filter:' + #placa + ':' + #clienteId + ':' + #modeloId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<MotoDTO> findByFilters(String placa, Long clienteId, Long modeloId, Pageable pageable) {
        // Se seu repository usa UPPER no JPQL, pode passar o texto cru aqui.
        return motoRepository.findByFilters(placa, clienteId, modeloId, pageable).map(this::convertToDTO);
    }

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
        if (!motoRepository.existsById(id)) {
            throw new EntityNotFoundException("Moto não encontrada com ID: " + id);
        }
        Moto moto = convertToEntity(motoDTO);
        moto.setId(id);
        moto.setPlaca(normalizePlaca(moto.getPlaca()));
        moto = motoRepository.save(moto);
        return convertToDTO(moto);
    }

    @Transactional
    @CacheEvict(value = "motos", allEntries = true)
    public void delete(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new EntityNotFoundException("Moto não encontrada com ID: " + id);
        }
        motoRepository.deleteById(id);
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
        moto.setPlaca(dto.getPlaca()); // normalizo no save/update

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));
            moto.setCliente(cliente);
        } else {
            moto.setCliente(null);
        }

        if (dto.getModeloMotoId() != null) {
            ModeloMoto modeloMoto = modeloMotoRepository.findById(dto.getModeloMotoId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Modelo de moto não encontrado com ID: " + dto.getModeloMotoId()));
            moto.setModeloMoto(modeloMoto);
        } else {
            moto.setModeloMoto(null);
        }

        return moto;
    }
}
