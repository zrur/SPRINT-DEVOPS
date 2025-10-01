package br.com.fiap.mottooth.repository;

import br.com.fiap.mottooth.model.Moto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long>, JpaSpecificationExecutor<Moto> {

    /* ======= Unicidade / buscas simples ======= */

    @EntityGraph(attributePaths = {"cliente", "modeloMoto"})
    Optional<Moto> findByPlacaIgnoreCase(String placa);

    boolean existsByPlacaIgnoreCase(String placa);

    boolean existsByPlacaIgnoreCaseAndIdNot(String placa, Long id);

    /* ======= Paginadas com relações carregadas ======= */

    @EntityGraph(attributePaths = {"cliente", "modeloMoto"})
    Page<Moto> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "modeloMoto"})
    Page<Moto> findByPlacaContaining(String placa, Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "modeloMoto"})
    Page<Moto> findByClienteId(Long clienteId, Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "modeloMoto"})
    Page<Moto> findByModeloMotoId(Long modeloMotoId, Pageable pageable);

    /* ======= Consultas com join (lista) ======= */

    /** Lista motos com cliente e modelo já carregados, sem duplicar IDs. */
    @Query("""
           select distinct m
           from Moto m
           left join fetch m.cliente
           left join fetch m.modeloMoto
           """)
    List<Moto> findAllWithRefs();

    /** Busca por ID já trazendo cliente e modelo. */
    @Query("""
           select m
           from Moto m
           left join fetch m.cliente
           left join fetch m.modeloMoto
           where m.id = :id
           """)
    Optional<Moto> findByIdWithRefs(@Param("id") Long id);

    /** Filtro paginado (sem fetch join, para não quebrar paginação). */
    @Query("""
           select distinct m
           from Moto m
           left join m.cliente c
           left join m.modeloMoto mm
           where (:placa is null or upper(m.placa) like concat('%', upper(:placa), '%'))
             and (:clienteId is null or c.id = :clienteId)
             and (:modeloId is null or mm.id = :modeloId)
           """)
    Page<Moto> findByFilters(
            @Param("placa") String placa,
            @Param("clienteId") Long clienteId,
            @Param("modeloId") Long modeloId,
            Pageable pageable
    );
}
