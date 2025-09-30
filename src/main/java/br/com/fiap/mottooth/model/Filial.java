package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TB_FILIAL")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_FILIAL",
        sequenceName = "SEQ_FILIAL", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FILIAL")
    @Column(name = "ID_FILIAL")
    private Long id;

    @NotBlank(message = "O nome da filial é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_PATIO", nullable = false)
    private Patio patio;

    @OneToMany(mappedBy = "filial")
    private List<Departamento> departamentos;

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

    public Patio getPatio() {
        return patio;
    }

    public void setPatio(Patio patio) {
        this.patio = patio;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }
}
