package com.techbirdssolutions.springpos.config;

import com.opencsv.CSVReader;
import lombok.Getter;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PrivilegeListConfig {
    private String filePath;

    @Getter
    private List<String> privilegeList;

    ResourceLoader resourceLoader;

    private PrivilegeListConfig(@Autowired ResourceLoader resourceLoader,@Value("${settings.privilegeListFilePath}") String filePath) {
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
                    .map(row -> row[0])
                    .collect(Collectors.toList());
            log.info("Privilege List loaded successfully Size: {}", privilegeList.size());
        } catch (Exception e) {
            log.error("Error loading privilege list from file: {}", filePath);
            log.error("Error: {}", ExceptionUtils.getStackTrace(e));
        }

    }
}
