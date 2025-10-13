package br.com.fiap.mottooth.repository;

import br.com.fiap.mottooth.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    // Associações: navegue até o id com underscore
    List<Movimentacao> findByMoto_Id(Long motoId);

    List<Movimentacao> findByUsuario_Id(Long usuarioId);

    List<Movimentacao> findByTipoMovimentacao_Id(Long tipoMovimentacaoId);

    // Campo simples na entidade
    List<Movimentacao> findByDataMovimentacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    // NOVO: último movimento da moto (para bloquear consecutivos iguais)
    Optional<Movimentacao> findTopByMoto_IdOrderByDataMovimentacaoDesc(Long motoId);

    // >>> NOVO: usado para bloquear exclusão de motos com movimentações
    boolean existsByMoto_Id(Long motoId);
}
