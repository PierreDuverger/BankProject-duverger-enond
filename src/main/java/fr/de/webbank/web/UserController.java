package fr.de.webbank.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.de.webbank.entity.User;
import fr.de.webbank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(username);

        log.info("l'utilisateur %s est connecté.", username);
        if (byEmail.isPresent() && byEmail.get().getPassword().equals(password)) {
            ObjectMapper mapper = new ObjectMapper();
            String userJson = mapper.writeValueAsString(byEmail.get());
            Cookie tokenCookie = new Cookie("USER", URLEncoder.encode(userJson));
//        tokenCookie.setHttpOnly(true);
            tokenCookie.setPath("/");
//        tokenCookie.setSecure(true);
            tokenCookie.setMaxAge(-1);
//        tokenCookie.setDomain("localhost");
            response.addCookie(tokenCookie);
            response.sendRedirect("/banque");
        } else {
            response.sendRedirect("/#!/login/error");
        }
    }
}
