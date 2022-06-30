import decode from "jwt-decode";

import CustosService from "./index";

const ACCESS_TOKEN_KEY = 'access_token';
const ID_TOKEN_KEY = 'id_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

export function hasTokenExpired(token) {
    const expirationDate = getTokenExpirationDate(token);
    return !!expirationDate && expirationDate < new Date();
}

export function getTokenExpirationDate(encodedToken) {
    try {
        if (!encodedToken) {
            return null;
        }

        const token = decode(encodedToken);
        if (!token.exp) {
            return null;
        }

        const date = new Date(0);
        date.setUTCSeconds(token.exp);
        return date;
    } catch (err) {
        return null;
    }
}

export default class CustosIdentity {
    /**
     * @type {CustosService}
     */
    _custosService = null;
    changeListeners = [];

    constructor(custosService) {
        this._custosService = custosService;

        window.addEventListener('storage', (e) => {
            for (let i = 0; i < this.changeListeners.length; i++) {
                this.changeListeners[i] && typeof this.changeListeners[i] === "function" && this.changeListeners[i](e);
            }
        });
    }

    authenticated() {
        return !!(this.idToken && !hasTokenExpired(this.idToken));
    }

    currentUsername() {
        if (this.accessToken) {
            try {
                let {preferred_username} = decode(this.accessToken);
                return preferred_username;
            } catch (err) {
                return null
            }
        } else {
            return null
        }
    }

    get accessToken() {
        return localStorage.getItem(ACCESS_TOKEN_KEY);
    }

    set accessToken(accessToken) {
        if (accessToken == null) {
            localStorage.removeItem(ACCESS_TOKEN_KEY);
        } else {
            localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
        }
    }

    get refreshToken() {
        return localStorage.getItem(REFRESH_TOKEN_KEY);
    }

    set refreshToken(refreshToken) {
        if (refreshToken == null) {
            localStorage.removeItem(REFRESH_TOKEN_KEY);
        } else {
            localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
        }
    }

    get idToken() {
        return localStorage.getItem(ID_TOKEN_KEY);
    }

    set idToken(idToken) {
        if (idToken == null) {
            localStorage.removeItem(ID_TOKEN_KEY);
        } else {
            localStorage.setItem(ID_TOKEN_KEY, idToken);
        }
    }

    get custosService() {
        return this._custosService;
    }

    onChange(changeListener) {
        this.changeListeners.push(changeListener);
    }

    async getOpenIdConfig() {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.IDENTITY}/.well-known/openid-configuration`,
            {
                params: {'client_id': this.custosService.clientId}
            }
        );
    }

    async getCILogonAuthenticationUrl({ciLogonInstitutionEntityId = null} = {}) {
        const {data: {authorization_endpoint}} = await this.getOpenIdConfig();
        let url = `${authorization_endpoint}?response_type=code&client_id=${this.custosService.clientId}&redirect_uri=${this.custosService.redirectURI}&scope=openid`;

        if (ciLogonInstitutionEntityId) {
            url += `&kc_idp_hint=oidc&idphint=${ciLogonInstitutionEntityId}`;
        } else {
            url += `&kc_idp_hint=oidc`;
        }

        return url;
    }

    _saveTokenResponse(response) {
        const {data: {access_token, id_token, refresh_token}} = response;

        this.accessToken = access_token;
        this.idToken = id_token;
        this.refreshToken = refresh_token;

        return response;
    }

    async getToken({code}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.IDENTITY}/token`,
            {'code': code, 'redirect_uri': this.custosService.redirectURI, 'grant_type': 'authorization_code'}
        ).then(this._saveTokenResponse.bind(this));
    }

    async localLogin({username, password}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.IDENTITY}/token`,
            {'grant_type': 'password', 'username': username, 'password': password}
        ).then(this._saveTokenResponse.bind(this));
    }

    async logout() {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.IDENTITY}/user/logout`,
            {refresh_token: this.refreshToken}
        ).then(() => {
            this.accessToken = null;
            this.idToken = null;
            this.refreshToken = null;
        })
    }

    async getTokenUsingRefreshToken() {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.IDENTITY}/token`,
            {'refresh_token': this.custosService.identity.refreshToken, 'grant_type': 'refresh_token'}
        ).then(this._saveTokenResponse.bind(this));
    }

    getClientSecret({clientId}) {
        return this.custosService.axiosInstanceWithTokenAuthorization.get(
            `${CustosService.ENDPOINTS.IDENTITY}/credentials`,
            {
                params: {
                    "client_id": clientId
                }
            }
        ).then(({data: {custos_client_secret}}) => custos_client_secret);
    }

}
