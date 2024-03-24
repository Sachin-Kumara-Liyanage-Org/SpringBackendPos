package com.techbirdssolutions.springpos.config;

import com.techbirdssolutions.springpos.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.UUID;

/**
 * This class implements the HandlerInterceptor interface to log incoming requests and outgoing responses.
 * It generates a unique ID for each request and logs the method, URI, URL, user, and address of the request.
 * It also logs the status and content type of the response.
 * The unique ID is stored in the MDC (Mapped Diagnostic Context) to associate log messages with the request.
 */
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);


    /**
     * This method is called before the request is handled.
     * It generates a unique ID, stores it in the MDC, and logs the method, URI, URL, user, and address of the request.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param handler the handler object
     * @return true to indicate that the request should be handled; false otherwise
     * @throws Exception if an error occurs
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        MDC.put(CommonConstant.UNIQUE_ID_MDC_KEY, uniqueId);
        logger.info("Incoming request : \n{\n\tUUID: {} \n\tMethod:{} \n\tURI:{} \n\tURL:{} \n\tUser:{} \n\tAddress:{} \n}",
                uniqueId, request.getMethod(), request.getRequestURI(),request.getRequestURL().toString(),request.getRemoteUser(),request.getRemoteAddr());
        return true;
    }

    /**
     * This method is called after the request is completed.
     * It logs the status and content type of the response and removes the unique ID from the MDC.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param handler the handler object
     * @param ex the Exception (if any) that was thrown during the handling of the request
     * @throws Exception if an error occurs
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String uniqueId = MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY);
        logger.info("Outgoing response: \n{\n\tUUID: {} \n\tStatus:{} \n\tContent Type:{} \n}", uniqueId,response.getStatus(),response.getContentType() );
        MDC.remove(CommonConstant.UNIQUE_ID_MDC_KEY);
    }
}
