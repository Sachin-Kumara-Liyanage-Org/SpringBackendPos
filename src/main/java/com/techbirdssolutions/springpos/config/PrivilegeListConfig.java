package com.techbirdssolutions.springpos.config;

import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is used to load and manage a list of privileges from a CSV file.
 * The CSV file is specified by the filePath property, which is injected from the application settings.
 * The class provides getters for the list of privileges, the list of privilege names, and the set of privilege categories.
 * The class also provides a nested class, PrivilegeCsvRow, which represents a row in the CSV file.
 */
@Component
@Slf4j
public class PrivilegeListConfig {
    private String filePath;

    @Getter
    private List<PrivilegeCsvRow> privilegeList;

    @Getter
    private List<String> privilegeNameList;

    @Getter
    private Set<String> privilegeCategoryList;

    ResourceLoader resourceLoader;

    private String categoryHeader;
    /**
     * Constructs a new PrivilegeListConfig object.
     * The constructor injects the ResourceLoader and the filePath property, and then calls the loadPrivilegeList method to load the privileges from the CSV file.
     *
     * @param resourceLoader the ResourceLoader
     * @param filePath the path to the CSV file
     */
    private PrivilegeListConfig(@Autowired ResourceLoader resourceLoader,@Value("${settings.privilegeListFilePath}") String filePath) {
        this.categoryHeader = null;
        this.privilegeCategoryList = new HashSet<>();
        this.privilegeNameList = new ArrayList<>();
        this.resourceLoader = resourceLoader;
        this.filePath = filePath;
        loadPrivilegeList();
    }
    /**
     * Loads the privileges from the CSV file.
     * The method reads the CSV file, maps each row to a PrivilegeCsvRow object, and collects the objects into the privilegeList.
     * The method also collects the privilege names and categories into the privilegeNameList and privilegeCategoryList, respectively.
     * If an error occurs while reading the file, the method logs the error.
     */
    private void loadPrivilegeList(){
        log.info("Loading Privilege List from file: {}", filePath);
        privilegeList = new ArrayList<>();
        try(InputStream fileStream=resourceLoader.getResource(filePath).getInputStream();
            InputStreamReader inputReader = new InputStreamReader(fileStream);
            CSVReader reader = new CSVReader(inputReader)) {
            privilegeList = reader.readAll().stream()
                    .map(row -> {
                        if (categoryHeader == null) {
                            categoryHeader = row[1];
                        }
                        privilegeCategoryList.add(row[1]);
                        privilegeNameList.add(row[0]);
                        return new PrivilegeCsvRow(row[0], row[1], parseBoolean(row[2]));
                    }
                    )
                    .collect(Collectors.toList());
            privilegeList.remove(0); // remove header
            privilegeCategoryList.remove(categoryHeader); // remove header
            privilegeNameList.remove(0); // remove header
            log.info("Privilege List loaded successfully Size: {}", privilegeList.size());
        } catch (Exception e) {
            log.error("Error loading privilege list from file: {}", filePath);
            log.error("Error: {}", ExceptionUtils.getStackTrace(e));
        }

    }
    /**
     * Parses a string to a boolean.
     * The method returns true if the string is "true" (ignoring case), and false otherwise.
     *
     * @param value the string to parse
     * @return the parsed boolean
     */
    private boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("true");
    }

    /**
     * This class represents a row in the CSV file.
     * Each row contains a privilege name, a privilege category, and a boolean indicating whether the privilege is for super admin only.
     */
    @Getter
    @AllArgsConstructor
    public class PrivilegeCsvRow {
        private String name;
        private String category;
        private boolean superAdminOnly;
    }
}
