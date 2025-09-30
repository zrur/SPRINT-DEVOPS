package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TB_PAIS")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_PAIS",
        sequenceName = "SEQ_PAIS", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAIS")
    @Column(name = "ID_PAIS")
    private Long id;

    @NotBlank(message = "O nome do país é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @OneToMany(mappedBy = "pais")
    private List<Estado> estados;

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

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }
}
