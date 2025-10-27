package br.com.fiap.mottooth.service;

import br.com.fiap.mottooth.model.Usuario;
import br.com.fiap.mottooth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario salvar(Usuario usuario) {
        // Aqui poderia haver criptografia de senha se quiser implementar
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setEmail(usuarioAtualizado.getEmail());
                    usuario.setSenha(usuarioAtualizado.getSenha());
                    usuario.setTipoUsuario(usuarioAtualizado.getTipoUsuario());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
