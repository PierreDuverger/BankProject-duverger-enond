import angular from 'angular';
import LigneBancaire from "./banque/ligne-bancaire";
import User from "./banque/user";
import "../style/app.css";import "jquery";

import LignesBancaireComponent from './banque/ligne-bancaire';
import UserComponent from './banque/user';
import uirouter from '@uirouter/angularjs';

import "@fortawesome/fontawesome-free/css/all.min.css";

import {default as userServiceName, UserService} from "../service/UserService";
import {default as lignebancaireServiceName, LigneBancaireService} from "../service/LigneBancaireService";
// Declare banque level module which depends on views, and core components
angular.module('app', [uirouter])
    .component(LigneBancaire.name, LigneBancaire.component)
    .component(User.name, User.component)
    .service(userServiceName, UserService)
    .service(lignebancaireServiceName, LigneBancaireService)
    .config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {


        const lignebancaire = {
            name: "lignebancaire",
            state: {
                url: "/lignebancaire",
                views: {
                    'main@': {
                        component: LignesBancaireComponent.name
                    }
                }
            }
        };

        const lignesbancaireUser = {
            name: "user",
            state: {
                url: "/lignebancaire/user?id",
                views: {
                    'main@': {
                        component: UserComponent.name
                    }
                }
            }
        };



        $urlRouterProvider.otherwise("/lignebancaire");

        $stateProvider
            .state(lignebancaire.name, lignebancaire.state);

        $stateProvider
            .state(lignesbancaireUser.name, lignesbancaireUser.state)

    }])


angular.bootstrap(document.body, ['app']);