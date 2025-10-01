package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "TB_MOTO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_MOTO_PLACA", columnNames = "PLACA")
        }
)
@SequenceGenerator(
        name = "SEQ_MOTO",
        sequenceName = "SEQ_MOTO",
        allocationSize = 1
)
public class Moto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOTO")
    @Column(name = "ID_MOTO")
    private Long id;

    @NotBlank(message = "A placa é obrigatória")
    @Size(max = 10, message = "A placa deve ter no máximo 10 caracteres")
    @Pattern(regexp = "[A-Z0-9]+", message = "A placa deve conter apenas letras maiúsculas e números")
    @Column(name = "PLACA", length = 10, nullable = false)
    private String placa;

    @Column(name = "DATA_REGISTRO")
    private LocalDateTime dataRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", foreignKey = @ForeignKey(name = "FK_MOTO_CLIENTE"))
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODELO_MOTO", foreignKey = @ForeignKey(name = "FK_MOTO_MODELO"))
    private ModeloMoto modeloMoto;

    // Vários beacons podem apontar para a mesma moto
    @OneToMany(mappedBy = "moto", fetch = FetchType.LAZY, cascade = {})
    private List<Beacon> beacons = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.dataRegistro == null) this.dataRegistro = LocalDateTime.now();
        normalize();
    }

    @PreUpdate
    public void preUpdate() {
        normalize();
    }

    /** Normaliza placa: trim + upper, evitando duplicidade por variação. */
    private void normalize() {
        if (this.placa != null) {
            this.placa = this.placa.trim().toUpperCase();
        }
    }

    /* Getters/Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public ModeloMoto getModeloMoto() { return modeloMoto; }
    public void setModeloMoto(ModeloMoto modeloMoto) { this.modeloMoto = modeloMoto; }

    public List<Beacon> getBeacons() { return beacons; }
    public void setBeacons(List<Beacon> beacons) { this.beacons = beacons; }

    /* equals/hashCode somente por id */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Moto)) return false;
        Moto other = (Moto) o;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return 31; }
}
