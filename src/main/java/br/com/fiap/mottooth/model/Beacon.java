package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Entity
@Table(name = "TB_BEACON")
@SequenceGenerator(
        name = "SEQ_BEACON",
        sequenceName = "SEQ_BEACON",
        allocationSize = 1
)
public class Beacon implements Serializable {

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

    // Muitos beacons podem referenciar a mesma moto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MOTO", foreignKey = @ForeignKey(name = "FK_BEACON_MOTO"))
    private Moto moto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODELO_BEACON", foreignKey = @ForeignKey(name = "FK_BEACON_MODELO"))
    private ModeloBeacon modeloBeacon;

    /* Getters/Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public Integer getBateria() { return bateria; }
    public void setBateria(Integer bateria) { this.bateria = bateria; }

    public Moto getMoto() { return moto; }
    public void setMoto(Moto moto) { this.moto = moto; }

    public ModeloBeacon getModeloBeacon() { return modeloBeacon; }
    public void setModeloBeacon(ModeloBeacon modeloBeacon) { this.modeloBeacon = modeloBeacon; }

    /* equals/hashCode somente por id */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Beacon)) return false;
        Beacon other = (Beacon) o;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return 31; }
}
