package com.techbirdssolutions.springpos.config;
import com.techbirdssolutions.springpos.entity.MetaSettings;
import com.techbirdssolutions.springpos.entity.customenum.MetadataTypes;
import com.techbirdssolutions.springpos.repository.MetaSettingsRepository;
import org.springframework.transaction.annotation.Transactional;
import com.techbirdssolutions.springpos.entity.Privilege;
import com.techbirdssolutions.springpos.entity.Role;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.repository.PrivilegeRepository;
import com.techbirdssolutions.springpos.repository.RoleRepository;
import com.techbirdssolutions.springpos.repository.UserRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@Transactional

public class DefaultDataLoad {

    @Autowired
    private PrivilegeListConfig privilegeListConfig;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private MetaSettingsRepository metaSettingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String DEFAULT_USER_EMAIL = "test@test.com";

    @Getter
    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    @Getter
    private static final String PASSWORD_PREFIX = "Auto_";
    @Getter
    private static final String META_EXP_DATE_KEY = "ExpDate";

    @Transactional
    public boolean runDefaultDataLoad() {
        try {
            log.info("\u001B[32mIs New Super Admin Role Add :{}\u001B[0m", insertSuperAdminRole());
            log.info("\u001B[32mIs New Privileges Found :{}\u001B[0m", insertMissingPrivileges());
            log.info("\u001B[32mIs New Test User Add :{}\u001B[0m", insertTestUser());
            log.info("\u001B[32mIs Add Exp Date :{}\u001B[0m", insertExpDate());
            return true;
        } catch (Exception e) {
            log.error("Error in Default Data Load: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    private boolean insertExpDate() {
        if(metaSettingsRepository.findByName(META_EXP_DATE_KEY)==null){
            LocalDateTime localDateTime = LocalDateTime.now().plusDays(30);
            log.info("\u001B[32mExp Date not found in DB, Adding: {} : {}\u001B[0m", META_EXP_DATE_KEY,localDateTime);
            metaSettingsRepository.save(MetaSettings.builder().name(META_EXP_DATE_KEY).date(localDateTime).type(MetadataTypes.DATE_TIME).build());
            return true;
        }
        log.info("\u001B[32mExp Date Already Exists in DB, Skipping...\u001B[0m");
        return false;
    }

    @Transactional
    private boolean insertMissingPrivileges() {

        List<Privilege> privilegeList = privilegeRepository.findAll();
        List<String> privilegeListFromFile = privilegeListConfig.getPrivilegeList();
        List<Privilege> newPrivilege = new ArrayList<>();
        boolean isPrivilegeAdded = false;
        for (String privilege : privilegeListFromFile) {
            if (privilegeList.stream().noneMatch(p -> p.getName().equals(privilege))) {
                log.info("\u001B[33mPrivilege not found in DB, Adding: {}\u001B[0m", privilege);
                Privilege privilegeObj = new Privilege(privilege);
                privilegeRepository.save(privilegeObj);
                log.info("\u001B[32mif{}\u001B[0m",privilegeObj.getId());
                newPrivilege.add(privilegeObj);

                isPrivilegeAdded = true;
            }
        }
        if (isPrivilegeAdded) {
            log.info("\u001B[32mPrivileges Added Successfully\u001B[0m");
            Role role = roleRepository.findByName(SUPER_ADMIN);
            if(role.getPrivileges()==null){
                role.setPrivileges(new ArrayList<>());
            }
            role.getPrivileges().addAll(newPrivilege);
            roleRepository.save(role);
            log.info("\u001B[32mPrivileges Added to Role Successfully\u001B[0m");
        } else {
            log.info("\u001B[32mPrivileges Already Exists in DB, Skipping...\u001B[0m");
        }
        return isPrivilegeAdded;
    }
    @Transactional
    private boolean insertSuperAdminRole() {
        if (roleRepository.findByName(SUPER_ADMIN) == null) {
            log.info("\u001B[32mSuper Admin Role not found in DB, Added Successfully\u001B[32m");
            roleRepository.save(new Role(SUPER_ADMIN));
            return true;
        }
        log.info("\u001B[32mSuper Admin Role Already Exists in DB, Skipping...\u001B[32m");
        return false;
    }
    @Transactional
    private boolean insertTestUser() {
        Role role = roleRepository.findByName(SUPER_ADMIN);
        User user = userRepository.findByEmail(DEFAULT_USER_EMAIL);
        String password = UUID.randomUUID().toString();
        boolean isUserAdded = false;
        if (user == null) {
            log.info("Test User not found in DB, Adding");
            user = new User();
            user.setEmail(DEFAULT_USER_EMAIL);
            user.setPassword(PASSWORD_PREFIX + passwordEncoder.encode(password));
            user.setFirstName("Test");
            user.setLastName("Test");
            user.setRoles(List.of(role));
            user.setEnabled(true);
            userRepository.save(user);
            log.warn("\u001B[31m New Test User Added with \n{\n\tEmail:{}\n\tPassword: {}\n}\u001B[0m",
                    DEFAULT_USER_EMAIL, password);
            isUserAdded = true;
        } else if (user.getPassword() == null || user.getPassword().isEmpty()
                || user.getPassword().startsWith(PASSWORD_PREFIX)) {
            log.info("\u001B[33mTest User found in DB with default password, Changing Password\u001B[0m");
            user.setPassword(PASSWORD_PREFIX + passwordEncoder.encode(password));
            userRepository.save(user);
            log.warn("\u001B[31m Test User Updated with \n{\n\tEmail:{}\n\tPassword: {}\n}\u001B[0m",
                    DEFAULT_USER_EMAIL, password);
            isUserAdded = true;
        }
        if (isUserAdded) {
            log.warn("\u001B[31m Please change the password for the user: {}\u001B[0m", DEFAULT_USER_EMAIL);
        } else {
            log.info("\u001B[32m Test User already exists in DB with Custom Password, Skipping...\u001B[0m");
        }

        return isUserAdded;
    }

}
