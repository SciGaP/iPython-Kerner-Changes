package org.apache.airavata.jupyter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class Authenticator implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Authorization header {}", authorizationHeader);
        return true;
    }
}
