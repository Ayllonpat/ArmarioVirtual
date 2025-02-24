package com.trianasalesianos.dam.ArmarioVirtual.security.jwt;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getJwtAccessTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtService.validateAccessToken(token)) {
            UUID usuarioId = jwtService.getUserIdFromAccessToken(token);

            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtService.TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtService.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtService.TOKEN_PREFIX.length());
        }
        return null;
    }
}
