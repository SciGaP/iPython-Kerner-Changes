import CustosService from "./index";

export default class CustosUsers {
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

    async registerUser({username, firstName, lastName, password, email}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.USERS}/user`,
            {
                'client_id': this.custosService.clientId,
                'username': username,
                'first_name': firstName,
                'last_name': lastName,
                'password': password,
                'temporary_password': false,
                'email': email
            }
        );
    }

    async enableUser({clientId, username}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.USERS}/user/activation`,
            {
                'username': username
            }
        );
    }

    async disableUser({clientId, username}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.USERS}/user/deactivation`,
            {
                'username': username
            }
        );
    }

    async checkUsernameValidity({username}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.USERS}/user/availability`,
            {
                params: {'username': username}
            }
        );
    }

    findUsers({offset = 0, limit = 20, username = null, groupId = null, clientId = null}) {
        if (groupId) {
            return this.custosService.axiosInstanceWithTokenAuthorization.get(
                `${CustosService.ENDPOINTS.GROUPS}/user/group/memberships/child`,
                {
                    params: {"group.id": groupId, "client_id": clientId}
                }
            ).then(({data: {profiles}}) => {
                return profiles;
            });
        } else {
            return this.custosService.axiosInstanceWithTokenAuthorization.get(
                `${CustosService.ENDPOINTS.USERS}/users`,
                {
                    params: {offset: offset, limit: limit, client_id: clientId, 'user.id': username}
                }
            ).then(({data: {users}}) => {
                return users;
            });
        }
    }

    /**
     * @typedef {Object} UserAttribute
     * @param {string} key
     * @param {string[]} values
     */

    /**
     * Add User Attributes
     * @param {UserAttribute[]} attributes
     * @param {string[]} usernames
     * @return {Promise<AxiosResponse<any>>}
     */
    addUserAttribute({clientId, attributes, usernames}) {
        return this.custosService.axiosInstanceWithTokenAuthorization.post(
            `${CustosService.ENDPOINTS.USERS}/attributes`,
            {
                client_id: clientId,
                attributes: attributes,
                users: usernames
            }
        );
    }

    /**
     * Delete User Attributes
     * @param {UserAttribute[]} attributes
     * @param {string[]} usernames
     * @return {Promise<AxiosResponse<any>>}
     */
    deleteUserAttributes({clientId, attributes, usernames}) {
        return this.custosService.axiosInstanceWithTokenAuthorization.delete(
            `${CustosService.ENDPOINTS.USERS}/attributes`,
            {
                data: {
                    client_id: clientId,
                    attributes: attributes,
                    users: usernames
                }
            }
        );
    }


    /**
     * Add Roles to User
     * @param {string[]} roles
     * @param {string[]} usernames
     * @param {boolean} isClientLevel
     * @return {Promise<AxiosResponse<any>>}
     */
    async addRolesToUser({clientId, roles, usernames, clientLevel = false}) {
        const axiosInstance = await this.custosService.axiosInstanceWithTokenAuthorization;
        return axiosInstance.post(
            `${CustosService.ENDPOINTS.USERS}/users/roles`,
            {
                client_id: clientId,
                roles: roles,
                usernames: usernames,
                client_level: clientLevel
            }
        );
    }

    /**
     * Delete Roles From User
     * @param {string[]} clientRoles
     * @param {string} username
     * @return {Promise<AxiosResponse<any>>}
     */
    deleteRolesFromUser({clientId, roles, username, clientLevel}) {
        return this.custosService.axiosInstanceWithTokenAuthorization.delete(
            `${CustosService.ENDPOINTS.USERS}/user/roles`,
            {
                data: {
                    client_id: clientId,
                    roles: roles,
                    username: username,
                    client_level: clientLevel
                }
            }
        );
    }

    async updateProfile({clientId, username, firstName, lastName, email}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization({clientId});
        return axiosInstance.put(
            `${CustosService.ENDPOINTS.USERS}/user/profile`,
            {
                username: username,
                first_name: firstName,
                last_name: lastName,
                email: email
            }
        ).then(({data}) => data);
    }

    getTenantLevelRoles() {
        return this.getRoles({isClientLevel: false});
    }

    getClientLevelRoles() {
        return this.getRoles({isClientLevel: true});
    }

    async getRoles({isClientLevel = false}) {
        const axiosInstance = await this.custosService.getAxiosInstanceWithClientAuthorization();
        return axiosInstance.get(
            `${CustosService.ENDPOINTS.USERS}/roles`,
            {
                params: {client_level: isClientLevel}
            }
        );
    }
}
