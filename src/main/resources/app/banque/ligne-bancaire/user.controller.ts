import {UserService, default as userServiceName} from "../../service/UserService";

export default class UserCtrl {

    private static readonly $inject=[
        userServiceName,
        "$stateParams"
    ]
    private user: any;

    constructor(private userService:UserService, private $stateParams) {
    }

    $onInit() {
        this.userService.getUser(this.$stateParams.id)
            .then((response) => {
                console.log("user",response);
                this.user = response;
            });
    }

}