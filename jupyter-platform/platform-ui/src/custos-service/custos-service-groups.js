import CustosService from "./index";

export default class CustosGroups {
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

    /**
     * Create Groups
     * @param {string} name
     * @param {string} description
     * @param {number} ownerId
     * @param {string[]} realm_roles
     * @param {string[]} client_roles
     * @param {string[]} attributes
     * @param {string[]} sub_groups
     * @return {Promise<AxiosResponse<any>>}
     */
    createGroup({clientId, name, description, ownerId, realm_roles, client_roles, attributes, sub_groups}) {
        return this.custosService.axiosInstanceWithTokenAuthorization.post(
            `${CustosService.ENDPOINTS.GROUPS}/group`,
            {
                clientId: clientId,
                group: {
                    name,
                    description,
                    ownerId,
                    realm_roles,
                    client_roles,
                    attributes,
                    sub_groups
                }
            }
        ).then(({data}) => data);
    }

    /**
     * Update Group
     * @param {number} groupId
     * @param {string} name
     * @param {string} description
     * @param {number} ownerId
     * @param {string[]} realm_roles
     * @param {string[]} client_roles
     * @param {string[]} attributes
     * @param {string[]} sub_groups
     * @return {Promise<AxiosResponse<any>>}
     */
    async updateGroup({groupId, name, description, ownerId, realm_roles, client_roles, attributes, sub_groups}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.put(
            `${CustosService.ENDPOINTS.GROUPS}/group/${groupId}`,
            {groupId, name, description, ownerId, realm_roles, client_roles, attributes, sub_groups}
        );
    }

    /**
     * Delete Group
     * @param {number} groupId
     * @return {Promise<AxiosResponse<any>>}
     */
    async deleteGroup({clientId, groupId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.delete(
            `${CustosService.ENDPOINTS.GROUPS}/group/${groupId}`
        );
    }

    /**
     * Find Group
     * @param {number} groupId
     * @return {Promise<AxiosResponse<any>>}
     */
    async findGroup({clientId, groupId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.GROUPS}/group`,
            {
                params: {
                    "group.id": groupId
                }
            }
        ).then(({data}) => data);
    }

    /**
     * GET All Groups
     * @return {Promise<AxiosResponse<any>>}
     */
    getAllGroups(
        {
            // offset = 0, limit = 50,
            groupId = null,
            // tenantId = null,
            clientId = null, username
        } = {}
    ) {
        if (username) {
            return this.custosService.axiosInstanceWithTokenAuthorization.get(
                `${CustosService.ENDPOINTS.GROUPS}/user/group/memberships`,
                {
                    params: {
                        clientId,
                        "profile.username": username
                    }
                }
            );
        } else {
            return this.custosService.axiosInstanceWithTokenAuthorization.get(
                `${CustosService.ENDPOINTS.GROUPS}/groups`,
                {
                    params: {
                        // offset: offset,
                        // limit: limit,
                        "group.id": groupId,
                        clientId
                    }
                }
            );
        }

    }

    /**
     * Add User to Group
     * @param {number} groupId
     * @param {string} username
     * @param {string} membershipType
     * @return {Promise<AxiosResponse<any>>}
     */
    async addUserToGroup({clientId, groupId, username, membershipType}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.GROUPS}/user/group/membership`,
            {
                group_id: groupId,
                username: username,
                membership_type: membershipType
            }
        );
    }

    /**
     * Remove User From Group
     * @param {number} groupId
     * @param {string} username
     * @return {Promise<AxiosResponse<any>>}
     */
    async removeUserFromGroup({clientId, groupId, username}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.delete(
            `${CustosService.ENDPOINTS.GROUPS}/user/group/membership`,
            {
                data: {
                    group_id: groupId,
                    username: username
                }
            }
        );
    }

    /**
     * Change Group Membership
     * @param {number} groupId
     * @param {string} username
     * @param {string} membershipType
     * @return {Promise<AxiosResponse<any>>}
     */
    async changeGroupMembership({groupId, username, membershipType}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.put(
            `${CustosService.ENDPOINTS.GROUPS}/user/group/membership`,
            {
                group_id: groupId,
                username: username,
                type: membershipType
            }
        );
    }

    /**
     * Add Child Group
     * @param {number} childGroupId
     * @param {number} parentGroupId
     * @return {Promise<AxiosResponse<any>>}
     */
    async addChildGroup(childGroupId, parentGroupId) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.GROUPS}/group/membership`,
            {
                child_id: childGroupId,
                parent_id: parentGroupId
            }
        );
    }

    /**
     * Remove Child Group
     * @param {number} childGroupId
     * @param {number} parentGroupId
     * @return {Promise<AxiosResponse<any>>}
     */
    async removeChildGroup({childGroupId, parentGroupId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.delete(
            `${CustosService.ENDPOINTS.GROUPS}/group/membership`,
            {
                data: {
                    child_id: childGroupId,
                    parent_id: parentGroupId
                }
            }
        );
    }

    /**
     * Get All Child Users
     * @param {number} groupId
     * @return {Promise<AxiosResponse<any>>}
     */
    async getAllChildUsers({groupId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.GROUPS}/user/group/memberships/child`,
            {
                params: {"group.id": groupId}
            }
        );
    }

    /**
     * Get All Child Groups
     * @param {number} groupId
     * @return {Promise<AxiosResponse<any>>}
     */
    async getAllChildGroups({groupId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.GROUPS}/groups/memberships/child`,
            {
                params: {"group.id": groupId}
            }
        );
    }

    /**
     * Get All Groups of User
     * @param {string} username
     * @return {Promise<AxiosResponse<any>>}
     */
    async getAllGroupsOfUser({username}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.GROUPS}/user/group/memberships`,
            {
                params: {"profile.username": username}
            }
        );
    }

    /**
     * Get All Parent Groups of Group
     * @param {number} groupId
     * @return {Promise<AxiosResponse<any>>}
     */
    async getAllParentGroupsOfGroup({groupId}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.GROUPS}/groups/memberships`,
            {
                params: {"group.id": groupId}
            }
        );
    }

    /**
     * Has Access
     * @param {number} groupId
     * @param {string} username
     * @param {string} membershipType
     * @return {Promise<AxiosResponse<any>>}
     */
    async hasAccess({groupId, username, membershipType}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.GROUPS}/user/group/access`,
            {
                params: {
                    "group.id": groupId,
                    "username": username,
                    "type": membershipType
                }
            }
        );
    }
}
