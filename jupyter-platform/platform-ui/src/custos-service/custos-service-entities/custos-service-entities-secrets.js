import CustosService from "../index";
import CustosServiceEntitiesSecretsSSH from "./custos-service-entities-secrets-ssh";
import CustosServiceEntitiesSecretsPassword from "./custos-service-entities-secrets-password";

export default class CustosServiceEntitiesSecrets {
    /**
     * @type {CustosService}
     */
    _custosService = null;

    /**
     * @type {CustosServiceEntitiesSecretsSSH}
     */
    _ssh = null;

    /**
     * @type {CustosServiceEntitiesSecretsPassword}
     */
    _password = null;

    constructor(custosService) {
        this._custosService = custosService;
        this._ssh = new CustosServiceEntitiesSecretsSSH(this.custosService)
        this._password = new CustosServiceEntitiesSecretsPassword(this.custosService)
    }

    get custosService() {
        return this._custosService;
    }

    getSecretModel({type}) {
        if (type === "SSH") {
            return this._ssh;
        } else if (type === "PASSWORD") {
            return this._password;
        }
    }

    async createSecret({type, clientId, description, ownerId, password}) {
        return this.getSecretModel({type}).createSecret({clientId, description, ownerId, password});
    }

    async getSecret({clientId, entityId}) {
        const secretMetaData = await this.getSecretMetadata({clientId, entityId});
        return this.getSecretModel({type: secretMetaData.type}).getSecret({clientId, entityId});
    }

    async getSecretMetadata({clientId, entityId = []}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        const {data: {metadata}} = await axiosInstance.get(
            `${CustosService.ENDPOINTS.SECRETS}/secret/summaries`,
            {
                params: {
                    client_id: clientId,
                    accessible_tokens: entityId
                }
            }
        );

        return metadata[0];
    }
}
