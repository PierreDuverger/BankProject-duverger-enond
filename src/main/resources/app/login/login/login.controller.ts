
import {UserService, default as userServiceName} from "../../service/UserService";

export default class LoginCtrl {
    private id: string;
    private password: string;


    private static readonly $inject = ["$state", userServiceName];

    constructor(private $state, private userService:UserService) {
    }

    $onInit() {
        this.logged();
    }

    async login() {
        let data = new FormData();
        data.append("username", this.id);
        data.append("password", this.password);
        await this.userService.getCurrentUser();
        this.userService.login(data)
            .then((response) => {
                if (response.status === 200)
                    document.location.href = "/livredor";
            })
    }

    logged() {
        this.userService.getCurrentUser()
            .then((user) => {
                if (user) {
                    this.logged = user;
                    document.location.href="/livredor";
                }

            })
    }
}