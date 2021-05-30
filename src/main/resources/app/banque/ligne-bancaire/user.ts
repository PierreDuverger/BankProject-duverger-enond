import './user.css';

const template = require("./user.html");

import controller from "./user.controller";

const component={
    template,
    controller,
    controllerAs: 'ctrl',
}

const UserComponent = {
    name: 'user',
    component
};


export default UserComponent;