package com.openwebinars.todo.security;

import com.openwebinars.todo.error.CustomAccessDeniedHandler;
import com.openwebinars.todo.error.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(excep -> {
                    excep.accessDeniedHandler(accessDeniedHandler);
                    excep.authenticationEntryPoint(authenticationEntryPoint);
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**",
                                "/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                        // Control por roles
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/gestor/**").hasAnyRole("ADMIN", "GESTOR") // El Gestor gestiona categorías
                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "GESTOR", "USUARIO")

                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                );

        http.headers((headers) ->
                headers.frameOptions((opts) -> opts.disable()));
        return http.build();
    }


}
