package fr.de.webbank;

import fr.de.webbank.entity.LigneBancaire;
import fr.de.webbank.entity.User;
import fr.de.webbank.repository.LigneBancaireRepository;
import fr.de.webbank.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Component
class Initializer implements CommandLineRunner {

    private final UserRepository repository;
    private final LigneBancaireRepository ligneBancaireRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Initializer(UserRepository repository, LigneBancaireRepository repo, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.ligneBancaireRepository = repo;
        this.bCryptPasswordEncoder = encoder;
    }

    @Override
    public void run(String... strings) {
        String[] libelles = {"Virement Maman", "Uber Eat", "Super U", "O'Tacos", "Izly","Bar Le Convivial","Chez Alfred","Dentiste"};
        for (int i = 1; i < 5; i++) {
            User user = new User("test" + i, "test" + i + "@test.com", this.bCryptPasswordEncoder.encode("test" + i));
            if(i==1){
                user.setAdmin(true);
            }
            repository.save(user);
            for( int j = 0; j<8; j++){
                LigneBancaire ligne = new LigneBancaire(null,new BigDecimal( i * 20), libelles[j], user);
                ligneBancaireRepository.save(ligne);
            }
        }

    }
}
