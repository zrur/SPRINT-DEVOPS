package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.model.Usuario;
import br.com.fiap.mottooth.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "CRUD de usuários do sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna a lista completa de usuários cadastrados")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // CADASTRAR NOVO
    // =========================
    @PostMapping
    @Operation(summary = "Cadastrar novo usuário", description = "Cadastra um novo usuário (endpoint público, sem autenticação)")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        if (usuarioService.emailExiste(usuario.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        Usuario salvo = usuarioService.salvar(usuario);
        return ResponseEntity.ok(salvo);
    }

    // =========================
    // ATUALIZAR
    // =========================
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um usuário")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario atualizado = usuarioService.atualizar(id, usuario);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================
    // DELETAR
    // =========================
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
