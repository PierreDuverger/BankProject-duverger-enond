package fr.de.webbank.config;

import fr.de.webbank.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UserDetailsService userService;
    private final Logger log = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig, UserDetailsService userService) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(jwtConfig.getHeader());

        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfig.getPrefix(), "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                User userDetails = (User) userService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("{} -> authenticated user {} session expired {}", request.getRequestURI(), username, claims.getExpiration());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

}