package com.techbirdssolutions.springpos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * AppConfig is a configuration class that implements WebMvcConfigurer.
 * It is annotated with @Configuration to indicate that it is a source of bean definitions.
 * The AppConfig class overrides the addInterceptors method from the WebMvcConfigurer interface to add custom interceptors.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * This method is used to add custom interceptors.
     * It overrides the addInterceptors method from the WebMvcConfigurer interface.
     * The method adds a RequestLoggingInterceptor to the InterceptorRegistry.
     *
     * @param registry the InterceptorRegistry to which the custom interceptor is added
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor());
    }

    /**
     * This method is used to create a new RequestLoggingInterceptor bean.
     * It is annotated with @Bean to indicate that it is a factory for creating beans.
     *
     * @return a new RequestLoggingInterceptor
     */
    @Bean
    public RequestLoggingInterceptor requestLoggingInterceptor() {
        return new RequestLoggingInterceptor();
    }
}