package com.ttn.ecommerce.config;

import com.ttn.ecommerce.model.Token;
import com.ttn.ecommerce.repository.TokenRepository;
import com.ttn.ecommerce.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = getJWTFromRequest(request);

        try {
            if (StringUtils.hasText(token)) {
                Token accessToken = tokenRepository.findByJwt(token).orElse(null);
                if (accessToken == null) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write("Token not found");
                    return;
                }
                if (jwtUtils.isTokenExpired(accessToken.getJwt())) {
                    tokenRepository.delete(accessToken);
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write("Token is expired");
                }
                String username = jwtUtils.validateTokenAndRetrieveSubject(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

