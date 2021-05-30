package fr.de.webbank.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LigneBancaire {

    @Id
    @GeneratedValue
    private Integer id;

    private BigDecimal montant;
    private String libelle;

    @OneToOne
    private User user;

}
