import './login.css';

const template = require("./login.html");

import controller from "./login.controller";

const component={
    template,
    controller,
    controllerAs: 'login'
}

const LoginComponent = {
    name: 'login',
    component
};

export default LoginComponent;