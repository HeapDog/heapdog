package io.heapdog.core.security.jwt;

import io.heapdog.core.exception.JwtValidationFailedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("authorization");

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String token = authHeader.split(" ")[1];
            try {
                Authentication authentication = authenticationManager.authenticate(JwtAuthenticationToken.unauthenticated(token));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtValidationFailedException e) {
                resolver.resolveException(request, response, null, e);
                return;
            }
        }
        filterChain.doFilter(request, response);

    }
}
