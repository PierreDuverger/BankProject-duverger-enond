import {bootstrap, module} from 'angular';
import Login from "./login/login";
//
import uirouter from '@uirouter/angularjs';
import LoginComponent from './login/login'
import {default as userServiceName, UserService} from "../service/UserService";

module('login', [
    uirouter,
])
    .component(Login.name, Login.component)
    .service(userServiceName, UserService)
    .config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {


        const login = {
            name: "login",
            state: {
                url: "/login",
                views: {
                    'main@': {
                        component: LoginComponent.name
                    }
                }
            }
        };

        const error = {
            name: "login.error",
            state: {
                url: "/error",
                views: {
                    'error@login': {
                        controller: function ($scope, $timeout, $state) {
                            $timeout(() => alert("Couple login/password incorrect!"), 100);
                            $state.go("login");
                        }
                    }
                }
            }
        };


        $urlRouterProvider.otherwise("/login");

        $stateProvider
            .state(login.name, login.state)
            .state(error.name, error.state)

    }])
;
bootstrap(document.body, ['login']);