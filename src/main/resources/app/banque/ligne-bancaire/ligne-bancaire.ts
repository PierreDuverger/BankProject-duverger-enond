import './lignebancaire.css';

const template = require("./ligne-bancaire.html");

import controller from "./lignebancaire.controller";

const component={
    template,
    controller,
    controllerAs: 'ctrl',
}

const LignesBancaireComponent = {
    name: 'lignebancaire',
    component
};

export default LignesBancaireComponent;