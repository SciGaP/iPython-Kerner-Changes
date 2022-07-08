import axios from "axios";
import http from "http";
import https from "https";
import CustosGroups from "./custos-service-groups";
import CustosUsers from "./custos-service-users";
import CustosIdentity from "./custos-service-identity";
import CustosSharing from "./custos-service-sharing";
import CustosEntities from "./custos-service-entities";

const httpAgent = new http.Agent({keepAlive: true});
const httpsAgent = new https.Agent({keepAlive: true});

export default class CustosService {
    static ENDPOINTS = {
        IDENTITY: "/identity-management/v1.0.0",
        USERS: "user-management/v1.0.0",
        GROUPS: "group-management/v1.0.0",
        TENANTS: "tenant-management/v1.0.0",
        SHARING: "sharing-management/v1.0.0",
        SECRETS: "resource-secret-management/v1.0.0"
    };

    /**
     * Api Client ID
     * @type {strong}
     * @private
     */
    _clientId = null;

    /**
     * Api Client Secret
     * @type {strong}
     * @private
     */
    _clientSecret = null;

    /**
     * Api Redirect URI
     * @type {strong}
     * @private
     */
    _redirectURI = null;

    /**
     * Api Base URL
     * @type {strong}
     * @private
     */
    _baseURL = null;


    /**
     * @type {CustosGroups}
     */
    _groups = null;


    /**
     * @type {CustosTenants}
     */
    _tenants = null;


    /**
     * @type {CustosUsers}
     */
    _users = null;

    /**
     * @type {CustosIdentity}
     */
    _identity = null;

    /**
     * @type {CustosSharing}
     */
    _sharing = null;

    /**
     * @type {CustosEntities}
     */
    _entities = null;

    constructor({clientId, clientSecret, redirectURI, baseURL}) {
        this._clientId = clientId;
        this._clientSecret = clientSecret;
        this._redirectURI = redirectURI;
        this._baseURL = baseURL;
        this._groups = new CustosGroups(this);
        this._users = new CustosUsers(this);
        this._identity = new CustosIdentity(this);
        this._sharing = new CustosSharing(this);
        this._entities = new CustosEntities(this);
    }

    get clientId() {
        return this._clientId;
    }

    get clientSecret() {
        return this._clientSecret;
    }

    get redirectURI() {
        return this._redirectURI;
    }

    get baseURL() {
        return this._baseURL;
    }

    get tenants() {
        return this._tenants;
    }

    get groups() {
        return this._groups;
    }

    get users() {
        return this._users;
    }

    get identity() {
        return this._identity;
    }

    get sharing() {
        return this._sharing;
    }

    get entities() {
        return this._entities;
    }

    get axiosInstance() {
        return axios.create({
            httpAgent,
            httpsAgent,
            baseURL: this.baseURL,
            withCredentials: false,
            headers: {
                'Accept': '*/*',
                'Content-Type': 'application/json'
            }
        });
    }

    async getAxiosInstanceWithClientAuthorization({clientId = null, clientSecret = null} = {}) {
        if (!clientId) {
            clientId = this.clientId
        }

        if (clientId === this.clientId) {
            clientSecret = this.clientSecret;
        } else if (!clientSecret) {
            clientSecret = await this.identity.getClientSecret({clientId});
        }

        return axios.create({
            httpAgent,
            httpsAgent,
            baseURL: this.baseURL,
            withCredentials: false,
            headers: {
                'Accept': '*/*',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${btoa(`${clientId}:${clientSecret}`)}`
            }
        });
    }

    get axiosInstanceWithClientAuthorization() {
        return this.getAxiosInstanceWithClientAuthorization({clientId: this.clientId, clientSecret: this.clientSecret});
        // return axios.create({
        //     httpAgent,
        //     httpsAgent,
        //     baseURL: this.baseURL,
        //     withCredentials: false,
        //     headers: {
        //         'Accept': '*/*',
        //         'Content-Type': 'application/json',
        //         'Authorization': `Bearer ${btoa(`${this.clientId}:${this.clientSecret}`)}`
        //     }
        // });
    }

    get axiosInstanceWithTokenAuthorization() {
        const instance = axios.create({
            httpAgent,
            httpsAgent,
            baseURL: this.baseURL,
            withCredentials: false,
            headers: {
                'Accept': '*/*',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.identity.accessToken}`
            }
        });

        instance.interceptors.response.use(response => {
            return response;
        }, async error => {
            const {config, response: {status}} = error;
            const originalRequest = config;

            if (status >= 400) {
                await this.identity.getTokenUsingRefreshToken();
                originalRequest.headers['Authorization'] = `Bearer ${this.identity.accessToken}`;
                return axios(originalRequest);
            } else {
                return Promise.reject(error);
            }
        });

        return instance;
    }
}

console.log("process ", process);
console.log("process.env ", process.env);
console.log("process.env.REACT_APP_CUSTOS_API_URL ", process.env.REACT_APP_CLIENT_ID);
console.log("process.env.REACT_APP_CUSTOS_API_URL ", process.env.REACT_APP_CLIENT_SEC);
console.log("process.env.REACT_APP_CUSTOS_API_URL ", process.env.REACT_APP_REDIRECT_URI);
console.log("process.env.REACT_APP_CUSTOS_API_URL ", process.env.REACT_APP_CUSTOS_API_URL);

export const custosService = new CustosService({
    clientId: process.env.REACT_APP_CLIENT_ID,
    clientSecret: process.env.REACT_APP_CLIENT_SEC,
    redirectURI: process.env.REACT_APP_REDIRECT_URI,
    baseURL: process.env.REACT_APP_CUSTOS_API_URL
});
