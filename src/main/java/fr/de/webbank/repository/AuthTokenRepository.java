package fr.de.webbank.repository;


import fr.de.webbank.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {

    List<AuthToken> findByUserId(Integer user);

}
