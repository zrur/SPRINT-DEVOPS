package br.com.fiap.mottooth.repository;

import br.com.fiap.mottooth.model.ModeloMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeloMotoRepository extends JpaRepository<ModeloMoto, Long> {

    /** Pega a primeira ocorrência caso existam duplicatas. */
    Optional<ModeloMoto> findFirstByNomeAndFabricante(String nome, String fabricante);
}
