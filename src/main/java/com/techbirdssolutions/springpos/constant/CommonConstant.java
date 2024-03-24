package com.techbirdssolutions.springpos.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to hold the common constants for the application.
 * It uses the @Value annotation to inject the values from the application properties.
 * The class also provides a method to check if the current profile is local or external.
 */
@Configuration
@Getter
public class CommonConstant {
    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.host:localhost}")
    private String serverHost;

    @Value("${spring.datasource.host}")
    private String databaseHost;
    @Value("${spring.datasource.port}")
    private String databasePort;
    @Value("${spring.datasource.dbName}")
    private String databaseName;
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateType;

    @Value("${spring.jpa.properties.hibernate.show-sql}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String formatSql;

    public static final String UNIQUE_ID_MDC_KEY = "uniqueId";

    /**
     * This method checks if the current profile is local or external.
     *
     * @return true if the profile is local or external; false otherwise
     */
    public boolean isLocal(){
        return profile.equals("local")||profile.equals("external");
    }
}
