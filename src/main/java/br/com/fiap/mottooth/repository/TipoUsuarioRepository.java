package br.com.fiap.mottooth.repository;

import br.com.fiap.mottooth.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    Optional<TipoUsuario> findByDescricaoIgnoreCase(String descricao);
}
