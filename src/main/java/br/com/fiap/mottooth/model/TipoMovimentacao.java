package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_TIPO_MOVIMENTACAO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_TIPO_MOVIMENTACAO",
        sequenceName = "SEQ_TIPO_MOVIMENTACAO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class TipoMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_MOVIMENTACAO")
    @Column(name = "ID_TIPO_MOVIMENTACAO")
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 50, message = "A descrição deve ter no máximo 50 caracteres")
    @Column(name = "DESCRICAO", length = 50, nullable = false)
    private String descricao;

    // =========================
    // Getters e Setters
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
