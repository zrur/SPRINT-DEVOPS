package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TB_CIDADE")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_CIDADE",
        sequenceName = "SEQ_CIDADE", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CIDADE")
    @Column(name = "ID_CIDADE")
    private Long id;

    @NotBlank(message = "O nome da cidade é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    private Estado estado;

    @OneToMany(mappedBy = "cidade")
    private List<Bairro> bairros;

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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Bairro> getBairros() {
        return bairros;
    }

    public void setBairros(List<Bairro> bairros) {
        this.bairros = bairros;
    }
}
