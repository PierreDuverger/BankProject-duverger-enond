package fr.de.webbank.service;

import fr.de.webbank.entity.LigneBancaire;
import fr.de.webbank.repository.LigneBancaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ligne-bancaire")
public class LigneBancaireService {

    @Autowired
    LigneBancaireRepository ligneBancaireRepository;


    @GetMapping
    public List<LigneBancaire> lignes(){
        return ligneBancaireRepository.findAll();
    }
}
