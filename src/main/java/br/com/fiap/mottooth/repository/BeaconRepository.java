package br.com.fiap.mottooth.repository;

import br.com.fiap.mottooth.model.Beacon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeaconRepository extends JpaRepository<Beacon, Long> {

    /* ========= Buscas simples ========= */

    Optional<Beacon> findByUuid(String uuid);

    Page<Beacon> findByUuidContaining(String uuid, Pageable pageable);

    Page<Beacon> findByMoto_Id(Long motoId, Pageable pageable);

    Page<Beacon> findByModeloBeacon_Id(Long modeloBeaconId, Pageable pageable);

    /* ========= Helpers pareamento 1–1 ========= */

    boolean existsByMoto_Id(Long motoId);

    Optional<Beacon> findFirstByMoto_Id(Long motoId);

    /* ========= Consultas com join ========= */

    /** Lista beacons trazendo moto e modelo com LEFT JOIN FETCH (1 linha por beacon). */
    @Query("""
           select distinct b
           from Beacon b
           left join fetch b.moto
           left join fetch b.modeloBeacon
           order by b.uuid
           """)
    List<Beacon> findAllWithRefs();

    /** Busca 1 beacon por ID já com moto e modelo carregados. */
    @Query("""
           select b
           from Beacon b
           left join fetch b.moto
           left join fetch b.modeloBeacon
           where b.id = :id
           """)
    Optional<Beacon> findByIdWithRefs(@Param("id") Long id);

    /** Busca 1 beacon por UUID já com moto e modelo carregados. */
    @Query("""
           select b
           from Beacon b
           left join fetch b.moto
           left join fetch b.modeloBeacon
           where upper(b.uuid) = upper(:uuid)
           """)
    Optional<Beacon> findByUuidWithRefs(@Param("uuid") String uuid);

    /** Filtro paginado (sem fetch). DISTINCT evita duplicatas. */
    @Query("""
           select distinct b
           from Beacon b
           left join b.moto m
           left join b.modeloBeacon mb
           where (:uuid is null or upper(b.uuid) like concat('%', upper(:uuid), '%'))
             and (:motoId is null or m.id = :motoId)
             and (:modeloId is null or mb.id = :modeloId)
           """)
    Page<Beacon> findByFilters(
            @Param("uuid") String uuid,
            @Param("motoId") Long motoId,
            @Param("modeloId") Long modeloId,
            Pageable pageable
    );
}
