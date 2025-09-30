package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOCALIZACAO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_LOCALIZACAO",
        sequenceName = "SEQ_LOCALIZACAO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOCALIZACAO")
    @Column(name = "ID_LOCALIZACAO")
    private Long id;

    @NotNull(message = "A posição X é obrigatória")
    @Column(name = "POSICAO_X", nullable = false)
    private BigDecimal posicaoX;

    @NotNull(message = "A posição Y é obrigatória")
    @Column(name = "POSICAO_Y", nullable = false)
    private BigDecimal posicaoY;

    @Column(name = "DATA_HORA")
    private LocalDateTime dataHora = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "ID_MOTO")
    private Moto moto;

    @ManyToOne
    @JoinColumn(name = "ID_PATIO")
    private Patio patio;

    // =========================
    // Getters e Setters
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(BigDecimal posicaoX) {
        this.posicaoX = posicaoX;
    }

    public BigDecimal getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(BigDecimal posicaoY) {
        this.posicaoY = posicaoY;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Moto getMoto() {
        return moto;
    }

    public void setMoto(Moto moto) {
        this.moto = moto;
    }

    public Patio getPatio() {
        return patio;
    }

    public void setPatio(Patio patio) {
        this.patio = patio;
    }
}
