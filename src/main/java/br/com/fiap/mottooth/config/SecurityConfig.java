package br.com.fiap.mottooth.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        // página inicial (index)
                        .requestMatchers("/", "/index").authenticated()
                        // áreas restritas
                        .requestMatchers("/motos/**", "/beacons/**", "/flows/**")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_OPERADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // após logar, cai no index
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DEV: senhas em texto puro, trocar para BCrypt futuramente
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JdbcUserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("""
            SELECT EMAIL AS username, SENHA AS password, 1 AS enabled
            FROM TB_USUARIO
            WHERE EMAIL = ?
        """);

        manager.setAuthoritiesByUsernameQuery("""
            SELECT u.EMAIL AS username, 'ROLE_' || TRIM(UPPER(t.DESCRICAO)) AS authority
            FROM TB_USUARIO u
            JOIN TB_TIPO_USUARIO t ON t.ID_TIPO_USUARIO = u.ID_TIPO_USUARIO
            WHERE u.EMAIL = ?
        """);

        manager.setEnableGroups(false);
        return manager;
    }
}
