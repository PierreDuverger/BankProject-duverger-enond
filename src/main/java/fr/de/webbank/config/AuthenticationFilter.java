package fr.de.webbank.config;

import fr.de.webbank.entity.AuthToken;
import fr.de.webbank.entity.User;
import fr.de.webbank.repository.AuthTokenRepository;
import fr.de.webbank.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class AuthenticationFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final UserService userService;
    private final AuthTokenRepository authTokenRepository;
    private final String authToken;

    public AuthenticationFilter(AuthTokenRepository authTokenRepository, UserService userService, String authToken) {
        this.authTokenRepository = authTokenRepository;
        this.userService = userService;
        this.authToken = authToken;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        String cookiePath = request.getContextPath() + "/";
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Cookie token = WebUtils.getCookie(request, authToken);
        if (token != null) {
            try {
                Optional<AuthToken> byId = authTokenRepository.findById(token.getValue());
                byId.ifPresent((authTokenValue) -> {
                    if (authTokenValue.getExpiredDate().after(new Date())) {
                        Integer userId = authTokenValue.getUserId();
                        Optional<User> userOpt = userService.findById(userId);
                        userOpt.ifPresent(user -> {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            log.info("{} {} : authenticated user {}", request.getMethod(), request.getRequestURI(), user.getUsername());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        });
                    } else {
                        deleteCookie(request, response, authToken);
                    }
                });

            } catch (Exception e) {
                deleteCookie(request, response, authToken);
                SecurityContextHolder.clearContext();
            }


        }
        chain.doFilter(request, response);
    }

}
