export function csrfInterceptor($location, $state) {
    return {
        request: async (config) => {
            // let csrfCookie = document.body.getAttribute("data-csrf-token");
            // var headerName = 'X-XSRF-TOKEN';
            // let init = {
            //     credentials: 'include',
            //     headers:{}
            // };
            // init.headers[headerName] = csrfCookie;
            // let response = await fetch("/login", init);
            // let headers = response.headers.get("my-csrf-token");

            // document.body.setAttribute("data-csrf-token", headers);
            // let csrfCookie = document.body.getAttribute("data-csrf-token");
            // config.headers[headerName] = csrfCookie;
            return config;
        },

        // response: (response) => {
        //     let headers = response.headers("MY-CSRF-TOKEN");
        //     document.body.setAttribute("data-csrf-token", headers);
        //     return response;
        // },
        responseError: (rejection) => {
            switch (rejection.status) {
                case 403 :
                case 401 :
                case 409 :
                    document.location.href.indexOf("/login") < 0 &&
                    (document.location.href = "/login");
                    break;
                case 423:
                    $state.go("login.error");
                    break;
            }
            return rejection;
        }

    };
}

export default "XsrfInterceptor";