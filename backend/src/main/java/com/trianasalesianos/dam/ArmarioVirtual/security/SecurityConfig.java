package com.trianasalesianos.dam.ArmarioVirtual.security;

import com.trianasalesianos.dam.ArmarioVirtual.security.exceptionhandling.JwtAccessDeniedHandler;
import com.trianasalesianos.dam.ArmarioVirtual.security.exceptionhandling.JwtAuthenticationEntryPoint;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Estos tres sin JWT
                        .requestMatchers(HttpMethod.POST,
                                "/api/usuarios/crear/cliente",
                                "/api/usuarios/auth/login",
                                "/api/usuarios/auth/refresh/token"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/auth/activate/**").permitAll()

                        // El resto de la API
                        .requestMatchers("/api/usuarios/crear/admin").hasRole("ADMIN")
                        .requestMatchers("/api/usuarios/me").hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/prendas/**", "/api/conjuntos/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/prendas/añadir", "/api/prendas/*/like",
                                "/api/conjuntos/añadir", "/api/conjuntos/*/like"
                        ).hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/api/prendas/*/likes", "/api/conjuntos/*/likes"
                        ).hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/prendas/*", "/api/conjuntos/*")
                        .hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/prendas/*", "/api/conjuntos/*")
                        .hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/api/usuarios/*/follow", "/api/usuarios/*/unfollow"
                        ).hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET,
                                "/api/usuarios/*/seguidores", "/api/usuarios/*/seguidos"
                        ).hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/api/tags",
                                "/api/prendas/*/comentarios", "/api/conjuntos/*/comentarios"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/tags/*", "/api/comentarios/*"
                        ).hasAnyRole("CLIENTE","ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain mvcFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/logout",
                                "/**/*.jsp",
                                "/css/**",
                                "/js/**",
                                "/resources/**",
                                "/uploads/**",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

}
