package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_CLIENTE")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_CLIENTE",
        sequenceName = "SEQ_CLIENTE", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLIENTE")
    @Column(name = "ID_CLIENTE")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", length = 100, nullable = false)
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
    @Pattern(regexp = "[0-9.\\-]+", message = "O CPF deve conter apenas números, pontos e traços")
    @Column(name = "CPF", length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "TELEFONE", length = 20)
    private String telefone;

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

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
