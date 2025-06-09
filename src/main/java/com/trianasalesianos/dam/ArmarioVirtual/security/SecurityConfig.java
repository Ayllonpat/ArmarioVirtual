package com.trianasalesianos.dam.ArmarioVirtual.security;

import com.trianasalesianos.dam.ArmarioVirtual.security.exceptionhandling.JwtAccessDeniedHandler;
import com.trianasalesianos.dam.ArmarioVirtual.security.exceptionhandling.JwtAuthenticationEntryPoint;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.authenticationProvider(authenticationProvider());
        return authBuilder.build();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST,
                        "/api/usuarios/crear/cliente",
                        "/api/usuarios/auth/login",
                        "/api/usuarios/auth/refresh/token",
                        "/api/usuarios/auth/activate/**"
                ).permitAll()
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/crear/admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/me").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/prendas/**", "/api/conjuntos/**").permitAll()
                .requestMatchers(HttpMethod.POST,
                        "/api/prendas/añadir", "/api/prendas/*/like",
                        "/api/conjuntos/añadir", "/api/conjuntos/*/like"
                ).hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/prendas/*/likes", "/api/conjuntos/*/likes")
                .hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/prendas/*", "/api/conjuntos/*")
                .hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/prendas/*", "/api/conjuntos/*")
                .hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.POST,
                        "/api/usuarios/*/follow",
                        "/api/usuarios/*/unfollow"
                ).hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET,
                        "/api/usuarios/*/seguidores",
                        "/api/usuarios/*/seguidos"
                ).hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/tags").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tags/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tags").permitAll()
                .requestMatchers(HttpMethod.POST,
                        "/api/tags/prendas/*/tags/*",
                        "/api/tags/conjuntos/*/tags/*"
                ).hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.DELETE,
                        "/api/tags/prendas/*/tags/*",
                        "/api/tags/conjuntos/*/tags/*"
                ).hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.POST,
                        "/api/prendas/*/comentarios",
                        "/api/conjuntos/*/comentarios"
                ).hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET,
                        "/api/prendas/*/comentarios",
                        "/api/conjuntos/*/comentarios"
                ).permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/comentarios/*")
                .hasAnyRole("CLIENTE", "ADMIN")
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
