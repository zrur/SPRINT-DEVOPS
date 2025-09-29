package br.com.fiap.mottooth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/swagger-ui.html","/swagger-ui/**","/api-docs/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/motos/**","/beacons/**","/flows/**").hasAnyRole("ADMIN","OPERADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.loginPage("/login").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout"))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // USUÁRIOS EM MEMÓRIA PARA TESTE
    // admin / operador  -> ROLE_ADMIN
    // operador / operador -> ROLE_OPERADOR
    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("operador"))
                .roles("ADMIN")
                .build();

        UserDetails operador = User.withUsername("operador")
                .password(encoder.encode("operador"))
                .roles("OPERADOR")
                .build();

        return new InMemoryUserDetailsManager(admin, operador);
    }
}
