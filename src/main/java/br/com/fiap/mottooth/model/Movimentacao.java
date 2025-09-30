package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MOVIMENTACAO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_MOVIMENTACAO",
        sequenceName = "SEQ_MOVIMENTACAO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOVIMENTACAO")
    @Column(name = "ID_MOVIMENTACAO")
    private Long id;

    @Column(name = "DATA_MOVIMENTACAO")
    private LocalDateTime dataMovimentacao = LocalDateTime.now();

    @Column(name = "OBSERVACAO", columnDefinition = "CLOB")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_MOTO")
    private Moto moto;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_MOVIMENTACAO")
    private TipoMovimentacao tipoMovimentacao;

    // =========================
    // Getters e Setters
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Moto getMoto() {
        return moto;
    }

    public void setMoto(Moto moto) {
        this.moto = moto;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }
}
