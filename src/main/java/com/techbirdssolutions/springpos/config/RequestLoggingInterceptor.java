package com.techbirdssolutions.springpos.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.UUID;

public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    private static final String UNIQUE_ID_MDC_KEY = "uniqueId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        MDC.put(UNIQUE_ID_MDC_KEY, uniqueId);
        logger.info("Incoming request : \n{\n\tUUID: {} \n\tMethod:{} \n\tURI:{} \n\tURL:{} \n\tUser:{} \n\tAddress:{} \n}",
                uniqueId, request.getMethod(), request.getRequestURI(),request.getRequestURL().toString(),request.getRemoteUser(),request.getRemoteAddr());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String uniqueId = MDC.get(UNIQUE_ID_MDC_KEY);
        logger.info("Outgoing response: \n{\n\tUUID: {} \n\tStatus:{} \n\tContent Type:{} \n}", uniqueId,response.getStatus(),response.getContentType() );
        MDC.remove(UNIQUE_ID_MDC_KEY);
    }
}
