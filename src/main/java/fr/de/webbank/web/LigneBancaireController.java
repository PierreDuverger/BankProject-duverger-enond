package fr.de.webbank.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.de.webbank.entity.LigneBancaire;
import fr.de.webbank.entity.User;
import fr.de.webbank.repository.LigneBancaireRepository;
import fr.de.webbank.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/lignebancaire")
public class LigneBancaireController {

    private LigneBancaireRepository ligneBancaireRepository;
    private UserRepository userRepository;

    public LigneBancaireController(LigneBancaireRepository repo, UserRepository userRepository) {
        this.ligneBancaireRepository = repo;
        this.userRepository = userRepository;
    }

    @GetMapping
    Collection<LigneBancaire> ligneBancaires() {
        return ligneBancaireRepository.findAll();
    }

    @PostMapping
    void save(@RequestBody LigneBancaire ligneBancaire) {
        ligneBancaireRepository.save(ligneBancaire);
    }

    @DeleteMapping
    void delete(@RequestParam Integer id, HttpServletRequest request, HttpServletResponse res) {
        if (request.getCookies() != null) {
            Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                    .filter(c -> "USER".equals(c.getName()))
                    .findAny();
            cookie.ifPresent((c) -> {
                ObjectMapper mapper = new ObjectMapper();
                User user = null;
                try {
                    user = mapper.readValue(URLDecoder.decode(c.getValue()), User.class);
                } catch (IOException e) {

                    try {
                        res.sendError(HttpStatus.UNAUTHORIZED.value());
                    } catch (IOException ex) {
                    }
                }
                Optional<User> byId = userRepository.findById(user.getId());
                byId.ifPresent(u -> {
                    if (u.isAdmin()) {
                        ligneBancaireRepository.delete(new LigneBancaire(id,null, null, null));
                        return;
                    }

                    try {
                        res.sendError(HttpStatus.UNAUTHORIZED.value());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

            });
        }
    }
}
