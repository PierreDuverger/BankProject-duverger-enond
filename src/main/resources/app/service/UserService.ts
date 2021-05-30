import * as angular from "angular";

export class UserService {
    private _user: any;

    constructor(private $state, private $http, private $q) {
    }

    set user(user) {
        this._user = user;
    }

    get user() {
        return this._user;
    }

    deconnecter() {
        this.user = undefined;
        this.$http.post('/api/user/logout', undefined, {credentials: 'include'})
            .then(() => {
                document.location.href = "/login";
            })
    }

    login(data) {
        return this.$http.post("/api/user/login", data, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
    }

    getCurrentUser() {

        return this.$http
            .get("/api/user/current", {
                credentials: 'include',
            })
            .then(resp => {
                if (resp.status === 200) {
                    this._user = resp.data;
                    return this._user;
                }
            })
            .catch((e) => {
            });
    }

    getUser(id){
        return this.$http
            .get(`/api/user/${id}`, {
                credentials: 'include',
            })
            .then(resp => {
                if (resp.status === 200) {
                    return resp.data;
                }
            })
            .catch((e) => {
            });
    }

}

export default "UserService";