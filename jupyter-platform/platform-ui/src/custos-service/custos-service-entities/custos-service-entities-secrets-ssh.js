import CustosService from "../index";

export default class CustosServiceEntitiesSecretsSSH {
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

    async createSecret({clientId, description, ownerId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        const {data: {token}} = await axiosInstance.post(
            `${CustosService.ENDPOINTS.SECRETS}/secret/ssh`,
            {
                "metadata": {
                    "client_id": clientId,
                    "description": description,
                    "owner_id": ownerId
                }
            }
        );
        const entityId = token;
        return entityId;
    }

    async getSecret({clientId, entityId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        const {data} = await axiosInstance.get(
            `${CustosService.ENDPOINTS.SECRETS}/secret/ssh`,
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
