package fr.de.webbank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import fr.de.webbank.entity.AuthToken;
import fr.de.webbank.entity.User;
import fr.de.webbank.repository.UserRepository;
import fr.de.webbank.service.UserService;
import org.eclipse.microprofile.jwt.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {


    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtConfig jwtConfig;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {

                UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

                String name = auth.getName();
                String password = auth.getCredentials().toString();


                UserDetails user = this.getUserDetailsService().loadUserByUsername(name);

                if (user == null || !bCryptPasswordEncoder().matches(password, user.getPassword())) {
                    throw new BadCredentialsException("Username/Password does not match for " + auth.getPrincipal());
                }

                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

            }
        };
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    AuthenticationSuccessHandler getAuthSuccessHandler() {
        AuthenticationSuccessHandler handler = (httpServletRequest, httpServletResponse, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final User user = (User) authentication.getPrincipal();
            Claims claims = Jwts.claims().setSubject(user.getUsername());

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                    .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                    .compact();
            ObjectMapper mapper = new ObjectMapper();
            String responseToken = mapper.writeValueAsString(new AuthToken(token));

            httpServletResponse.getWriter().append(responseToken);
        };
        return handler;
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return username -> {
            Objects.requireNonNull(username);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return user;
        };
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/*.js", "/*.html", "/*.css", "/*.woff2", "/*.woff", "/*.ttf").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/banque").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/ligne-bancaire").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtTokenAuthenticationFilter(jwtConfig, userDetailsService()), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(getAuthProvider())
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/banque")
                .failureUrl("/#!/login/error")
                .and()
                .httpBasic()
                .and()
                .logout()
                .logoutUrl("/api/user/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
        ;

    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:index.html");
        registry.addViewController("/login").setViewName("forward:login.html");
        registry.addViewController("/banque").setViewName("forward:banque.html");
    }
}
