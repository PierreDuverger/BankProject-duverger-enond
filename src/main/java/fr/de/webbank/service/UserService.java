package fr.de.webbank.service;

import fr.de.webbank.entity.User;
import fr.de.webbank.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Objects.requireNonNull(username);
        Optional<User> byEmail = userRepository.findByEmail(username);
        User user = byEmail
//                .orElse(new User());
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }

}