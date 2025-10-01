package br.com.fiap.mottooth.service;

import br.com.fiap.mottooth.dto.BeaconDTO;
import br.com.fiap.mottooth.model.Beacon;
import br.com.fiap.mottooth.model.ModeloBeacon;
import br.com.fiap.mottooth.model.Moto;
import br.com.fiap.mottooth.repository.BeaconRepository;
import br.com.fiap.mottooth.repository.ModeloBeaconRepository;
import br.com.fiap.mottooth.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeaconService {

    private final BeaconRepository beaconRepository;
    private final MotoRepository motoRepository;
    private final ModeloBeaconRepository modeloBeaconRepository;

    public BeaconService(BeaconRepository beaconRepository,
                         MotoRepository motoRepository,
                         ModeloBeaconRepository modeloBeaconRepository) {
        this.beaconRepository = beaconRepository;
        this.motoRepository = motoRepository;
        this.modeloBeaconRepository = modeloBeaconRepository;
    }

    /* ===================== READS ===================== */

    @Transactional(readOnly = true)
    @Cacheable(value = "beacons", key = "#id")
    public BeaconDTO findById(Long id) {
        Beacon beacon = beaconRepository.findByIdWithRefs(id)
                .orElseThrow(() -> new EntityNotFoundException("Beacon não encontrado com ID: " + id));
        return convertToDTO(beacon);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "beacons", key = "'uuid:' + #uuid")
    public BeaconDTO findByUuid(String uuid) {
        Beacon beacon = beaconRepository.findByUuidWithRefs(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Beacon não encontrado com UUID: " + uuid));
        return convertToDTO(beacon);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "beacons", key = "'page:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<BeaconDTO> findAll(Pageable pageable) {
        return beaconRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(
            value = "beacons",
            key = "'filter:' + #uuid + ':' + #motoId + ':' + #modeloId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize"
    )
    public Page<BeaconDTO> findByFilters(String uuid, Long motoId, Long modeloId, Pageable pageable) {
        return beaconRepository.findByFilters(uuid, motoId, modeloId, pageable).map(this::convertToDTO);
    }

    /* ===================== WRITES ===================== */

    @Transactional
    @CacheEvict(value = "beacons", allEntries = true)
    public BeaconDTO save(BeaconDTO beaconDTO) {
        Beacon beacon = convertToEntity(beaconDTO);
        beacon = beaconRepository.save(beacon);
        return convertToDTO(beacon);
    }

    @Transactional
    @CacheEvict(value = "beacons", allEntries = true)
    public BeaconDTO update(Long id, BeaconDTO beaconDTO) {
        if (!beaconRepository.existsById(id)) {
            throw new EntityNotFoundException("Beacon não encontrado com ID: " + id);
        }
        Beacon beacon = convertToEntity(beaconDTO);
        beacon.setId(id);
        beacon = beaconRepository.save(beacon);
        return convertToDTO(beacon);
    }

    @Transactional
    @CacheEvict(value = "beacons", allEntries = true)
    public void delete(Long id) {
        if (!beaconRepository.existsById(id)) {
            throw new EntityNotFoundException("Beacon não encontrado com ID: " + id);
        }
        beaconRepository.deleteById(id);
    }

    /* ===================== MAPEAMENTO ===================== */

    private BeaconDTO convertToDTO(Beacon beacon) {
        BeaconDTO dto = new BeaconDTO();
        dto.setId(beacon.getId());
        dto.setUuid(beacon.getUuid());
        dto.setBateria(beacon.getBateria());

        if (beacon.getMoto() != null) {
            dto.setMotoId(beacon.getMoto().getId());
            dto.setPlacaMoto(beacon.getMoto().getPlaca());
        }

        if (beacon.getModeloBeacon() != null) {
            dto.setModeloBeaconId(beacon.getModeloBeacon().getId());
            dto.setModeloNome(beacon.getModeloBeacon().getNome());
        }

        return dto;
    }

    private Beacon convertToEntity(BeaconDTO dto) {
        Beacon beacon = new Beacon();
        beacon.setId(dto.getId());
        beacon.setUuid(dto.getUuid());
        beacon.setBateria(dto.getBateria());

        if (dto.getMotoId() != null) {
            Moto moto = motoRepository.findById(dto.getMotoId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Moto não encontrada com ID: " + dto.getMotoId()));
            beacon.setMoto(moto);
        } else {
            beacon.setMoto(null);
        }

        if (dto.getModeloBeaconId() != null) {
            ModeloBeacon modeloBeacon = modeloBeaconRepository.findById(dto.getModeloBeaconId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Modelo de beacon não encontrado com ID: " + dto.getModeloBeaconId()));
            beacon.setModeloBeacon(modeloBeacon);
        } else {
            beacon.setModeloBeacon(null);
        }

        return beacon;
    }
}
