package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_BEACON")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_BEACON",
        sequenceName = "SEQ_BEACON", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Beacon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BEACON")
    @Column(name = "ID_BEACON")
    private Long id;

    @NotBlank(message = "O UUID do beacon é obrigatório")
    @Size(max = 100, message = "O UUID deve ter no máximo 100 caracteres")
    @Column(name = "UUID", length = 100, nullable = false, unique = true)
    private String uuid;

    @Min(value = 0, message = "O nível de bateria deve ser no mínimo 0")
    @Max(value = 100, message = "O nível de bateria deve ser no máximo 100")
    @Column(name = "BATERIA")
    private Integer bateria;

    @OneToOne
    @JoinColumn(name = "ID_MOTO", unique = true) // garante relação 1–1 no banco
    private Moto moto;

    @ManyToOne
    @JoinColumn(name = "ID_MODELO_BEACON")
    private ModeloBeacon modeloBeacon;

    // =========================
    // Getters e Setters
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getBateria() {
        return bateria;
    }

    public void setBateria(Integer bateria) {
        this.bateria = bateria;
    }

    public Moto getMoto() {
        return moto;
    }

    public void setMoto(Moto moto) {
        this.moto = moto;
    }

    public ModeloBeacon getModeloBeacon() {
        return modeloBeacon;
    }

    public void setModeloBeacon(ModeloBeacon modeloBeacon) {
        this.modeloBeacon = modeloBeacon;
    }
}
