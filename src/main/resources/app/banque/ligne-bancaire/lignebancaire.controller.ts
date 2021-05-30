import {UserService, default as userServiceName} from "../../service/UserService";
import {LigneBancaireService, default as lignebancaireServiceName} from "../../service/LigneBancaireService";

export default class LigneBancaireCtrl {

    private static readonly $inject=[
        userServiceName,
        "$sce",
        LigneBancaireService,
        "$state"
    ]
    private lignesbancaire: Array<any>;
    private newLigneBancaireText: string;

    constructor(private userService:UserService, private $sce, private lignebancaireService:LigneBancaireService, private $state) {
    }

    $onInit() {
        this.userService.getCurrentUser()
            .then((response) => {
                this.loadLignesBancaire();
            });
    }

    loadLignesBancaire() {
        return this.lignebancaireService.loadLignesBancaire()
            .then((response) => {
                this.lignesbancaire = response.data;
                return response;
            });
    }

    deleteLigneBancaire(lignebancaire){
        this.lignebancaireService.delete(lignebancaire)
            .then(()=>this.loadLignesBancaire());
    }

    async valider() {
        if (this.newLigneBancaireText) {
            await this.loadLignesBancaire();
            let response = await this.lignebancaireService.addLigneBancaire(this.newLigneBancaireText);
            if (response.status === 200) {
                this.closeNewComment();
            }
        } else {
            alert("Il faut saisir un texte.")
        }

    }

    showUser(user){
        this.$state.go("user", {id:user.id})
    }

    closeNewComment() {
        this.newLigneBancaireText = undefined;
        this.loadLignesBancaire();
    }

}