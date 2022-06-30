import axios from "axios";
import http from "http";
import https from "https";

import idplist from "./idplist.json";

const httpAgent = new http.Agent({keepAlive: true});
const httpsAgent = new https.Agent({keepAlive: true});

export default class CiLogonService {
    static ENDPOINTS = {};

    /**
     * Api Base URL
     * @type {strong}
     * @private 84756369194
     */
    _baseURL = null;

    constructor({baseURL}) {
        this._baseURL = baseURL;
    }

    get baseURL() {
        return this._baseURL;
    }

    getInstitutions() {
        // TODO
        // return this.axiosInstance.get("https://cilogon.org/idplist/").then(data => {
        //     return data.map(({DisplayName, EntityID, OrganizationName, RandS}) => {
        //         return {displayName: DisplayName, entityId: EntityID, organizationName: OrganizationName, randS: RandS}
        //     });
        // });

        return idplist;
    }

    getInstitutionLoginUrl() {
        // TODO
        return null;
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
}