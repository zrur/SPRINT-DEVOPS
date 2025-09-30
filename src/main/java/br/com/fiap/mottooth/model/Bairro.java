package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TB_BAIRRO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_BAIRRO",
        sequenceName = "SEQ_BAIRRO",
        allocationSize = 1
)
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BAIRRO")
    @Column(name = "ID_BAIRRO")
    private Long id;

    @NotBlank(message = "O nome do bairro é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_CIDADE", nullable = false)
    private Cidade cidade;

    @OneToMany(mappedBy = "bairro")
    private List<Logradouro> logradouros;

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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public List<Logradouro> getLogradouros() {
        return logradouros;
    }

    public void setLogradouros(List<Logradouro> logradouros) {
        this.logradouros = logradouros;
    }
}
