package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_LOGRADOURO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_LOGRADOURO",
        sequenceName = "SEQ_LOGRADOURO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOGRADOURO")
    @Column(name = "ID_LOGRADOURO")
    private Long id;

    @NotBlank(message = "O nome do logradouro é obrigatório")
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    @Column(name = "NOME", length = 255, nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_BAIRRO")
    private Bairro bairro;

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

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }
}
