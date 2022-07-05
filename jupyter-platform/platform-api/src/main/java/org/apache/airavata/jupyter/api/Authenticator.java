package org.apache.airavata.jupyter.api;

import org.apache.custos.clients.CustosClientProvider;
import org.apache.custos.identity.service.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class Authenticator implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);

    @Autowired
    private CustosClientProvider custosClientProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Authorization header {}", authorizationHeader);

        String prefix = "Bearer";
        String token = authorizationHeader.substring(prefix.length());
        String userToken =  token.trim();

        User user = custosClientProvider.getIdentityManagementClient().getUser(userToken);
        return true;
    }
}
