package br.com.fiap.mottooth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_USUARIO")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "SEQ_USUARIO",
        sequenceName = "SEQ_USUARIO", // certifique-se de criar essa sequence no Oracle
        allocationSize = 1
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
    @Column(name = "ID_USUARIO")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "NOME", length = 100, nullable = false)
    private String nome;

    @NotBlank(message = "A senha é obrigatória")
    @Size(max = 255, message = "A senha deve ter no máximo 255 caracteres")
    @Column(name = "SENHA", length = 255, nullable = false)
    private String senha;

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(name = "EMAIL", length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_USUARIO")
    private TipoUsuario tipoUsuario;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
