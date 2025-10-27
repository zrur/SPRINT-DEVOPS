package br.com.fiap.mottooth.config;

import br.com.fiap.mottooth.security.JwtAuthenticationFilter;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // =========================
    // 1) API (JWT)
    // =========================
    @Bean
    @Order(1)
    SecurityFilterChain apiSecurity(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http.securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/usuarios",          // cadastro via API permitido
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =========================
    // 2) WEB (Thymeleaf + form login)
    // =========================
    @Bean
    @Order(2)
    SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login", "/register", "/register/**",
                                "/css/**", "/js/**", "/img/**"
                        ).permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/", "/index").authenticated()
                        .requestMatchers("/motos/**", "/beacons/**", "/flows/**")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_OPERADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> res.sendRedirect("/login?error"))
                        .accessDeniedHandler((req, res, e) -> res.sendRedirect("/login?denied"))
                )
                .sessionManagement(sm -> sm.invalidSessionUrl("/login?session").maximumSessions(1))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // =========================
    // Beans auxiliares
    // =========================
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Mantém compatibilidade com senhas em texto (fiap25)
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JdbcUserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("""
            SELECT EMAIL AS username, SENHA AS password, 1 AS enabled
              FROM TB_USUARIO
             WHERE LOWER(TRIM(EMAIL)) = LOWER(TRIM(?))
        """);

        manager.setAuthoritiesByUsernameQuery("""
            SELECT u.EMAIL AS username, 'ROLE_' || TRIM(UPPER(t.DESCRICAO)) AS authority
              FROM TB_USUARIO u
              JOIN TB_TIPO_USUARIO t ON t.ID_TIPO_USUARIO = u.ID_TIPO_USUARIO
             WHERE LOWER(TRIM(u.EMAIL)) = LOWER(TRIM(?))
        """);

        manager.setEnableGroups(false);
        return manager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
