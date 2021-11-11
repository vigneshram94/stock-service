package com.fse.stock.filter;

import com.fse.stock.model.User;
import com.fse.stock.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        if (path.contains("/actuator") || path.contains("/swagger-ui") || path.contains("/v3/api-docs")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            final String authorizationHeader = request.getHeader("Authorization");
            String jwt = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
            } else {
                authEntryPointJwt.commence(request, response, new AuthenticationException("Authentication token not provided") {
                });
                filterChain.doFilter(request, response);
                return;
            }

            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                User user = jwtUtil.getUserDetailsFromJwtToken(jwt);

                LOGGER.info("{} User Authenticated with roles [{}]", user.getUsername(), user.getRole());

                List<GrantedAuthority> authorityList = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(user.getRole());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, authorityList);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            LOGGER.error("Cannot set user authentication: {}", e);
            authEntryPointJwt.commence(request, response, new AuthenticationException("ExpiredJwtException") {
            });
        } catch (SignatureException e) {
            LOGGER.error("Cannot set user authentication: {}", e);
            authEntryPointJwt.commence(request, response, new AuthenticationException("SignatureException") {
            });
        }

        filterChain.doFilter(request, response);
        this.resetAuthenticationAfterRequest();
    }

    private void resetAuthenticationAfterRequest() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
