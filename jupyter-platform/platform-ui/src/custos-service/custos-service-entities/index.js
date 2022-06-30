import CustosService from "../index";
import CustosServiceEntitiesSecrets from "./custos-service-entities-secrets";

export default class CustosEntities {
    /**
     * @type {CustosService}
     */
    _custosService = null;

    /**
     * @type {CustosServiceEntitiesSecrets}
     */
    _secrets = null;

    constructor(custosService) {
        this._custosService = custosService;
        this._secrets = new CustosServiceEntitiesSecrets(this.custosService);
    }

    get custosService() {
        return this._custosService;
    }

    get secrets() {
        return this._secrets;
    }

    async createEntity({clientId, entityId, parentId, name, description, type, ownerId, fullText, binaryData, secretType, password}) {
        if (type === "SECRET") {
            entityId = await this.custosService.entities.secrets.createSecret({
                type: secretType, clientId, description, ownerId, password
            })
        }

        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        await axiosInstance.post(
            `${CustosService.ENDPOINTS.SHARING}/entity`,
            {
                "client_id": clientId,
                "entity": {
                    "id": entityId,
                    "parent_id": parentId,
                    "name": name,
                    "description": description,
                    "type": type,
                    "owner_id": ownerId,
                    "full_text": fullText,
                    "binary_data": binaryData
                }
            }
        );

        return entityId;
    }

    async updateEntity({clientId, entityId, name, description, type, ownerId, fullText, binaryData}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.put(
            `${CustosService.ENDPOINTS.SHARING}/entity`,
            {
                "client_id": clientId,
                "entity": {
                    "id": entityId,
                    "name": name,
                    "description": description,
                    "type": type,
                    "owner_id": ownerId,
                    "full_text": fullText,
                    "binary_data": binaryData
                }
            }
        ).then(({data: {types}}) => types);
    }

    async deleteEntity({clientId, entityId, name, description, type, ownerId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.delete(
            `${CustosService.ENDPOINTS.SHARING}/entity`,
            {
                data: {
                    "client_id": clientId,
                    "entity": {
                        "id": entityId,
                        "name": name,
                        "description": description,
                        "type": type,
                        "owner_id": ownerId
                    }
                }
            }
        ).then(({data: {types}}) => types);
    }

    async getEntities({clientId, ownerId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.SHARING}/entities`,
            {
                "client_id": clientId,
                "owner_id": ownerId
            }
        ).then((res) => {
            return res.data.entity_array;
        });
    }

    async getEntity({clientId, entityId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        const {data} = await axiosInstance.get(
            `${CustosService.ENDPOINTS.SHARING}/entity`,
            {
                params: {"entity.id": entityId}
            }
        );
        if (data.type === "SECRET") {
            data.metadata = await this.custosService.entities.secrets.getSecretMetadata({clientId, entityId: data.id});
            data.ext = await this.custosService.entities.secrets.getSecret({clientId, entityId: data.id});
        }

        return data;
    }


}
