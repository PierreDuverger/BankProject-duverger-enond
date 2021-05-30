package fr.de.webbank.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.de.webbank.entity.LigneBancaire;
import fr.de.webbank.entity.User;
import fr.de.webbank.repository.LigneBancaireRepository;
import fr.de.webbank.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ligne-bancaire")
public class LigneBancaireController {

    private LigneBancaireRepository ligneBancaireRepository;

    public LigneBancaireController(LigneBancaireRepository repo) {
        this.ligneBancaireRepository = repo;
    }

    @GetMapping
    Collection<LigneBancaire> users() {
        List<LigneBancaire> all = ligneBancaireRepository.findAll();
        all.forEach(ligne->{
            ligne.getUser().setPassword(null);
        });
        return all;
    }
    @PostMapping
    void save(@RequestBody LigneBancaire ligneBancaire) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ligneBancaire.setUser(principal);
        ligneBancaireRepository.save(ligneBancaire);;
    }

    @DeleteMapping
    void delete(@RequestParam Integer id) {
        ligneBancaireRepository.delete(new LigneBancaire(id,null, null, null));
    }
}
