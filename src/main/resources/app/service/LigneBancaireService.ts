import * as angular from "angular";

export class LigneBancaireService {


    constructor(private $http) {
    }

    loadLignesBancaire() {
        // return fetch('/api/ligne-bancaire', {
        //   credentials: 'include',
        // }).then(r=>r.json());
        return this.$http.get('/api/ligne-bancaire', {
            credentials: 'include',
        });

    }

    delete(lignebancaire) {
        return this.$http.delete("/api/ligne-bancaire?id=" + lignebancaire.id,
            {
                credentials: 'include',
            })
    }

    addLigneBancaire(lignebancaire) {
        return this.$http.post('/api/ligne-bancaire', {text: lignebancaire},
            {
                credentials: 'include',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
            })
    }

}

export default "LigneBancaireService";