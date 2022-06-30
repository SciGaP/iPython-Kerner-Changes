import CustosService from "../index";

export default class CustosServiceEntitiesSecretsPassword {
    /**
     * @type {CustosService}
     */
    _custosService = null;

    constructor(custosService) {
        this._custosService = custosService;
    }

    get custosService() {
        return this._custosService;
    }

    async createSecret({clientId, description, ownerId, password}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        const {data: {token}} = await axiosInstance.post(
            `${CustosService.ENDPOINTS.SECRETS}/secret/password`,
            {
                "metadata": {
                    "client_id": clientId,
                    "description": description,
                    "owner_id": ownerId,
                    "password": password
                }
            }
        );
        const entityId = token;
        return entityId;
    }

    async getSecret({clientId, entityId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        const {data} = await axiosInstance.get(
            `${CustosService.ENDPOINTS.SECRETS}/secret/password`,
            {
                params: {
                    "client_id": clientId,
                    "token": entityId
                }
            }
        );

        return data;
    }
}
