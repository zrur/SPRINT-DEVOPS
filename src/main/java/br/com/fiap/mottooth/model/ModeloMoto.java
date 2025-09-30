package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_MODELO_MOTO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_MODELO_MOTO",
        sequenceName = "SEQ_MODELO_MOTO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class ModeloMoto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MODELO_MOTO")
    @Column(name = "ID_MODELO_MOTO")
    private Long id;

    @NotBlank(message = "O nome do modelo é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", length = 100, nullable = false)
    private String nome;

    @NotBlank(message = "O fabricante é obrigatório")
    @Size(max = 100, message = "O fabricante deve ter no máximo 100 caracteres")
    @Column(name = "FABRICANTE", length = 100, nullable = false)
    private String fabricante;

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

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
}
