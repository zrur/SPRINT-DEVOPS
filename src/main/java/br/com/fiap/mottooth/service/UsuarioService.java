package br.com.fiap.mottooth.service;

import br.com.fiap.mottooth.model.TipoUsuario;
import br.com.fiap.mottooth.model.Usuario;
import br.com.fiap.mottooth.repository.TipoUsuarioRepository;
import br.com.fiap.mottooth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==== CRUD usado pela API ====
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail().trim())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (usuario.getSenha() != null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        if (usuario.getDataCadastro() == null) {
            usuario.setDataCadastro(LocalDateTime.now());
        }
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(resolveOperador());
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario dados) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (dados.getNome() != null) u.setNome(dados.getNome());
        if (dados.getEmail() != null) u.setEmail(dados.getEmail());
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            u.setSenha(passwordEncoder.encode(dados.getSenha()));
        }
        if (dados.getTipoUsuario() != null) u.setTipoUsuario(dados.getTipoUsuario());

        return usuarioRepository.save(u);
    }

    @Transactional
    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // ==== Cadastro vindo do Thymeleaf ====
    @Transactional
    public Usuario salvarNovoUsuario(String email, String nome, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Email e senha são obrigatórios.");
        }
        String emailNorm = email.trim();
        if (usuarioRepository.existsByEmail(emailNorm)) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Usuario u = new Usuario();
        u.setEmail(emailNorm);
        u.setNome(nome == null ? "" : nome.trim());
        u.setSenha(passwordEncoder.encode(senha));
        u.setDataCadastro(LocalDateTime.now());
        u.setTipoUsuario(resolveOperador()); // garante OPERADOR mesmo que não exista ainda

        return usuarioRepository.save(u);
    }

    private TipoUsuario resolveOperador() {
        return tipoUsuarioRepository
                .findByDescricaoIgnoreCase("OPERADOR")
                .orElseGet(() -> {
                    TipoUsuario t = new TipoUsuario();
                    t.setDescricao("OPERADOR");
                    return tipoUsuarioRepository.save(t);
                });
    }
}
