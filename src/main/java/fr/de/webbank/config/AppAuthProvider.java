package fr.de.webbank.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

public class AppAuthProvider extends DaoAuthenticationProvider {


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppAuthProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

        String name = auth.getName();
        String password = auth.getCredentials()
                .toString();


        UserDetails user = this.getUserDetailsService().loadUserByUsername(name);


        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Username/Password does not match for " + auth.getPrincipal());
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;

    }
}
