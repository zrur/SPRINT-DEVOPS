package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_REGISTRO_BATERIA")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_REGISTRO_BATERIA",
        sequenceName = "SEQ_REGISTRO_BATERIA", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class RegistroBateria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REGISTRO_BATERIA")
    @Column(name = "ID_REGISTRO")
    private Long id;

    @Column(name = "DATA_HORA")
    private LocalDateTime dataHora = LocalDateTime.now();

    @Min(value = 0, message = "O nível de bateria deve ser no mínimo 0")
    @Max(value = 100, message = "O nível de bateria deve ser no máximo 100")
    @Column(name = "NIVEL_BATERIA")
    private Integer nivelBateria;

    @ManyToOne
    @JoinColumn(name = "ID_BEACON")
    private Beacon beacon;

    // =========================
    // Getters e Setters
    // =========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(Integer nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }
}
