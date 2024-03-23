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

    private PrivilegeListConfig(@Autowired ResourceLoader resourceLoader,@Value("${settings.privilegeListFilePath}") String filePath) {
        this.categoryHeader = null;
        this.privilegeCategoryList = new HashSet<>();
        this.privilegeNameList = new ArrayList<>();
        this.resourceLoader = resourceLoader;
        this.filePath = filePath;
        loadPrivilegeList();
    }

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

    private boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("true");
    }

    @Getter
    @AllArgsConstructor
    public class PrivilegeCsvRow {
        private String name;
        private String category;
        private boolean superAdminOnly;
    }
}
