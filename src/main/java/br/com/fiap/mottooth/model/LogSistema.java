package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOG_SISTEMA")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_LOG_SISTEMA",
        sequenceName = "SEQ_LOG_SISTEMA", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class LogSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOG_SISTEMA")
    @Column(name = "ID_LOG")
    private Long id;

    @NotBlank(message = "A ação é obrigatória")
    @Size(max = 100, message = "A ação deve ter no máximo 100 caracteres")
    @Column(name = "ACAO", length = 100, nullable = false)
    private String acao;

    @Column(name = "DATA_HORA")
    private LocalDateTime dataHora = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    // =========================
    // Getters e Setters
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
