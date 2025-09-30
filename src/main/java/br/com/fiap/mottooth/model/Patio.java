package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_PATIO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_PATIO",
        sequenceName = "SEQ_PATIO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Patio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PATIO")
    @Column(name = "ID_PATIO")
    private Long id;

    @NotBlank(message = "O nome do pátio é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", length = 100, nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_LOGRADOURO")
    private Logradouro logradouro;

    // =========================
    // Getters e Setters
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Logradouro getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }
}
