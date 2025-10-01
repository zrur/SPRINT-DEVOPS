package br.com.fiap.mottooth.repository;

import br.com.fiap.mottooth.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatioRepository extends JpaRepository<Patio, Long> {
    Optional<Patio> findFirstByNome(String nome); // usado pelo DataLoader
    Optional<Patio> findByNome(String nome);
}
