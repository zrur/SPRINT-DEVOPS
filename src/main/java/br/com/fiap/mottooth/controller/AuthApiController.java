package br.com.fiap.mottooth.controller;

import br.com.fiap.mottooth.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

record AuthRequest(String username, String password) {}
record AuthResponse(String token, String tokenType) { AuthResponse(String t){ this(t,"Bearer"); } }

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Login e geração de token JWT para acesso à API")
public class AuthApiController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;

    public AuthApiController(AuthenticationManager am, JwtTokenProvider tp) {
        this.authManager = am;
        this.tokenProvider = tp;
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        try {
            String username = req.username() == null ? "" : req.username().trim();
            String password = req.password() == null ? "" : req.password();

            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenProvider.generateToken(auth);
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, "Credenciais inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse(null, "Erro interno no servidor"));
        }
    }
}
