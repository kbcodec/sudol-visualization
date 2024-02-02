package com.sudol.SudolAPI.Controllers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class SpaWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI().toLowerCase();

        if (!isStaticResource(path) && !isApiEndpoint(path)) {
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isStaticResource(String path) {
        return path.startsWith("/static") || path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".jpg") || path.endsWith(".png");
    }

    private boolean isApiEndpoint(String path) {
        return path.startsWith("/api");
    }
}
