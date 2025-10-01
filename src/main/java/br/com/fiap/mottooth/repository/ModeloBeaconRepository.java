package br.com.fiap.mottooth.repository;

import br.com.fiap.mottooth.model.ModeloBeacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeloBeaconRepository extends JpaRepository<ModeloBeacon, Long> {

    /** Pega a primeira ocorrência caso existam duplicatas. */
    Optional<ModeloBeacon> findFirstByNomeAndFabricante(String nome, String fabricante);
}
