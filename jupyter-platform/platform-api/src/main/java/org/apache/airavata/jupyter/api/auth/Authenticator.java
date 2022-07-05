/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.airavata.jupyter.api.auth;

import org.apache.custos.clients.CustosClientProvider;
import org.apache.custos.identity.service.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Component
public class Authenticator implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);

    @Autowired
    private CustosClientProvider custosClientProvider;

    @Autowired
    private AuthCache authCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String authorizationHeader = request.getHeader("Authorization");

        String prefix = "Bearer";
        String token = authorizationHeader.substring(prefix.length());
        String userToken =  token.trim();

        Authentication authentication = authCache.getAuthentication(userToken);
        if (authentication == null) {

            User user = custosClientProvider.getIdentityManagementClient().getUser(userToken);
            authentication = new Authentication() {
                @Override
                public String getName() {
                    return user.getUsername();
                }

                @Override
                public boolean implies(Subject subject) {
                    return false;
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return user;
                }

                @Override
                public boolean isAuthenticated() {
                    return true;
                }

                @Override
                public void setAuthenticated(boolean b) throws IllegalArgumentException {

                }
            };
            authCache.cacheAuthentication(userToken, authentication);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }
}
