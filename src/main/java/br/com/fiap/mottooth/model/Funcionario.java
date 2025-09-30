package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TB_FUNCIONARIO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_FUNCIONARIO",
        sequenceName = "SEQ_FUNCIONARIO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FUNCIONARIO")
    @Column(name = "ID_FUNCIONARIO")
    private Long id;

    @NotBlank(message = "O nome do funcionário é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres")
    @Column(name = "CPF", nullable = false, length = 14)
    private String cpf;

    @NotBlank(message = "O cargo é obrigatório")
    @Size(max = 50, message = "O cargo deve ter no máximo 50 caracteres")
    @Column(name = "CARGO", nullable = false, length = 50)
    private String cargo;

    @Column(name = "DATA_ADMISSAO")
    private LocalDate dataAdmissao;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_DEPARTAMENTO", nullable = false)
    private Departamento departamento;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
