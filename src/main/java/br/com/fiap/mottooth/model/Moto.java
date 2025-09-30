package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MOTO")
@SequenceGenerator(
        name = "SEQ_MOTO",
        sequenceName = "SEQ_MOTO", // crie a sequence no Oracle
        allocationSize = 1
)
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOTO")
    @Column(name = "ID_MOTO")
    private Long id;

    @NotBlank(message = "A placa é obrigatória")
    @Size(max = 10, message = "A placa deve ter no máximo 10 caracteres")
    @Pattern(regexp = "[A-Z0-9]+", message = "A placa deve conter apenas letras maiúsculas e números")
    @Column(name = "PLACA", length = 10, nullable = false, unique = true)
    private String placa;

    @Column(name = "DATA_REGISTRO")
    private LocalDateTime dataRegistro;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_MODELO_MOTO")
    private ModeloMoto modeloMoto;

    // Dono da relação é Beacon (tem a FK ID_MOTO)
    @OneToOne(mappedBy = "moto", cascade = CascadeType.ALL)
    private Beacon beacon;

    /** Garante DATA_REGISTRO se vier nulo no insert */
    @PrePersist
    public void prePersist() {
        if (this.dataRegistro == null) {
            this.dataRegistro = LocalDateTime.now();
        }
    }

    // =========================
    // Getters e Setters
    // =========================
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

    public Beacon getBeacon() { return beacon; }
    public void setBeacon(Beacon beacon) { this.beacon = beacon; }
}
