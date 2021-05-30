export class UserService {

    constructor($state, $http, $q) {
        this.$state = $state;
        this.$http = $http;
        this.$q = $q;
        this.api = "/api/user"
    }

    set user(user) {
        this._user = user;
    }

    get user() {
        return this._user;
    }

    set token(token) {
        token ? sessionStorage.setItem("x-auth-token", token)
            : sessionStorage.clear();
        this._token = token;
    }

    get token() {
        return this._token || sessionStorage.getItem("x-auth-token");
    }

    login(user) {
        let data = new FormData();
        data.append("username", user.username);
        data.append("password", user.password);
        return this.$http
            .post(`/login`, data,
                {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
            .then(response => {
                if (response.status === 200 && response.data) {
                    this.token = response.data.token;
                    return this.$q.resolve();
                }
                this.$state.go("login.error");
                return this.$q.reject();
            })
            .catch(e => {
                console.log(e);
                this.$state.go("login.error");
            });
    }

    deconnecter() {
        this.user = undefined;
        this.token = undefined;
        document.location.href = "/";
    }

    getCurrentUser() {
        let response = this.token && (
            this.$http
                .get(`${this.api}/current`, {
                    headers: {
                        'Authorization': `Bearer ${this.token}`
                    }
                })
                .then(resp => {
                    if (resp.status === 200) {
                        this._user = resp.data;
                        return this._user;
                    }
                })
                .catch((e) => {
                })
        );
        return response || this.$q.resolve();
    }

}

export default "UserService"