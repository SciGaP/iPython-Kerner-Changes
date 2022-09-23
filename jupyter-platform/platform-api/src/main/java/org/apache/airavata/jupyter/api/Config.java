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

package org.apache.airavata.jupyter.api;

import org.apache.airavata.jupyter.api.auth.AuthCache;
import org.apache.airavata.jupyter.api.auth.Authenticator;
import org.apache.airavata.jupyter.core.OrchestrationEngine;
import org.apache.custos.clients.CustosClientProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @org.springframework.beans.factory.annotation.Value("${custos.host}")
    private String custosHost;

    @org.springframework.beans.factory.annotation.Value("${custos.port}")
    private int custosPort;

    @org.springframework.beans.factory.annotation.Value("${custos.client.id}")
    private String custosClientId;

    @org.springframework.beans.factory.annotation.Value("${custos.client.secret}")
    private String custosClientSecret;

    @org.springframework.beans.factory.annotation.Value("${super.user.mode}")
    private boolean superUserMode;

    @Bean
    OrchestrationEngine orchestrationEngine() {
        return new OrchestrationEngine();
    }

    @Bean
    AuthCache authCache() {
        return new AuthCache();
    }

    @Bean
    public CustosClientProvider custosClientsFactory() {
        return new CustosClientProvider.Builder().setServerHost(custosHost)
                .setServerPort(custosPort)
                .setClientId(custosClientId)
                .setClientSec(custosClientSecret).build();
    }

    @Autowired
    private Authenticator authenticator;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (superUserMode) {
            registry.addInterceptor(authenticator)
                    .excludePathPatterns("/api/archive/**")
                    .excludePathPatterns("/api/remote/run/**")
                    .excludePathPatterns("/api/admin/**")
                    .excludePathPatterns("/api/job/**")
                    .excludePathPatterns("/error");
        } else {
            registry.addInterceptor(authenticator)
                    .excludePathPatterns("/api/archive/**")
                    .excludePathPatterns("/api/remote/run/**")
                    .excludePathPatterns("/api/job/**")
                    .excludePathPatterns("/error");
        }
    }
}
