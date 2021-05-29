package fr.de.webbank.repository;

import fr.de.webbank.entity.LigneBancaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneBancaireRepository extends JpaRepository<LigneBancaire, Integer> {
}
