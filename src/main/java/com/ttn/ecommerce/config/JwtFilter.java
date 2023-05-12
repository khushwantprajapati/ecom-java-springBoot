package com.ttn.ecommerce.config;

import com.ttn.ecommerce.repository.TokenRepository;
import com.ttn.ecommerce.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
//            String jwt = authHeader.substring(7);
//            if (jwt.isBlank()) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
//            } else {
//                String email;
//                try {
//                    email = jwtUtils.validateTokenAndRetrieveSubject(jwt);
//                } catch (RuntimeException e) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
//                    return;
//                }
//                Token token;
//                try {
//                    token = tokenRepository.findByJwt(jwt)
//                            .orElseThrow(() -> new RuntimeException("Token not found"));
//                } catch (RuntimeException e) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
//                    return;
//                }
//                if (!token.getValid()) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
//                    return;
//                }
//                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
//                if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
}

