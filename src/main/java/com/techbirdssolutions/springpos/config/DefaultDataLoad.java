package com.techbirdssolutions.springpos.config;
import com.techbirdssolutions.springpos.constant.CommonConstant;
import com.techbirdssolutions.springpos.constant.CustomMataDataConstant;
import com.techbirdssolutions.springpos.constant.UserConstant;
import com.techbirdssolutions.springpos.entity.*;
import com.techbirdssolutions.springpos.entity.customenum.MetadataTypes;
import com.techbirdssolutions.springpos.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

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
    private PrivilegeCategoryRepository privilegeCategoryRepository;

    @Autowired
    private MetaSettingsRepository metaSettingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommonConstant commonConstant;



    private Map<String,PrivilegeCategory> privilegeCategoryMap;

    @Transactional
    public boolean runDefaultDataLoad() {
        privilegeCategoryMap = new HashMap<>();
        try {
            log.info("\u001B[32mIs New Super Admin Role Add :{}\u001B[0m", insertSuperAdminRole());
            log.info("\u001B[32mIs New Privileges category Found :{}\u001B[0m", insertMissingPrivilegesCategory());
            log.info("\u001B[32mIs New Privileges Found :{}\u001B[0m", insertMissingPrivileges());
            log.info("\u001B[32mIs New Test User Add :{}\u001B[0m", insertTestUser());
            log.info("\u001B[32mIs Add Exp Date :{}\u001B[0m", insertExpDate());
            log.info("\u001B[32mIs Remove Old Data :{}\u001B[0m", removeOldPrivileges());
            return true;
        } catch (Exception e) {
            log.error("Error in Default Data Load: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    private boolean removeOldPrivileges() {
        List<Privilege> privilegeList = privilegeRepository.findByNameNotIn(privilegeListConfig.getPrivilegeNameList());
        List<PrivilegeCategory> privilegeCategoryList = privilegeCategoryRepository.findByNameNotIn(privilegeListConfig.getPrivilegeCategoryList().stream().toList());
        boolean isPrivilegeRemoved = false;

        if(privilegeList.size()>0){
            log.info("\u001B[33mRemoving Old Privileges: {}\u001B[0m",privilegeList.size());
            for(Privilege privilege:privilegeList){
                for(Role role:privilege.getRoles()){
                    log.info("\u001B[33mRemoving {} access from role {}\u001B[0m",privilege.getName(),role.getName());
                    role.getPrivileges().remove(privilege);
                    roleRepository.save(role);
                }
                log.info("\u001B[33mRemoving Privilege Name:{}\u001B[0m",privilege.getName());
                privilege.getRoles().clear();
            }
            privilegeRepository.deleteAll(privilegeList);
            isPrivilegeRemoved = true;
        }
        if(privilegeCategoryList.size()>0){
            log.info("\u001B[33mRemoving Old Privileges Category: {}\u001B[0m",privilegeCategoryList.size());
            privilegeCategoryRepository.deleteAll(privilegeCategoryList);
            isPrivilegeRemoved = true;
        }
        return isPrivilegeRemoved;
    }

    private boolean insertMissingPrivilegesCategory() {
        boolean isPrivilegeCategoryAdded = false;
        List<PrivilegeCategory> privilegeCategoryList = privilegeCategoryRepository.findAll();
        for(String category:privilegeListConfig.getPrivilegeCategoryList()){
            if(privilegeCategoryList.stream().noneMatch(p->p.getName().equals(category))){
                log.info("\u001B[33mPrivilege Category not found in DB, Adding: {}\u001B[0m",category);
                PrivilegeCategory privilegeCategory = PrivilegeCategory.builder().name(category).privileges(new ArrayList<>()).build();
                privilegeCategoryRepository.save(privilegeCategory);
                this.privilegeCategoryMap.put(category,privilegeCategory);
                isPrivilegeCategoryAdded = true;
            }else{
                PrivilegeCategory privilegeCategory = privilegeCategoryRepository.findByName(category);
                this.privilegeCategoryMap.put(category,privilegeCategory);
            }
        }
        return isPrivilegeCategoryAdded;
    }

    private boolean insertExpDate() {
        MetaSettings metaSettings = metaSettingsRepository.findByName(CustomMataDataConstant.META_EXP_DATE_KEY);
        if(metaSettings ==null){
            LocalDateTime localDateTime = LocalDateTime.now().plusDays(30);
            log.info("\u001B[32mExp Date not found in DB, Adding: {} : {}\u001B[0m", CustomMataDataConstant.META_EXP_DATE_KEY,localDateTime);
            metaSettingsRepository.save(MetaSettings.builder().name(CustomMataDataConstant.META_EXP_DATE_KEY).date(localDateTime).type(MetadataTypes.DATE_TIME).build());
            return true;
        }else if(Boolean.TRUE.equals(commonConstant.isLocal())){
            LocalDateTime localDateTime = LocalDateTime.now().plusDays(30);
            log.info("\u001B[32mExp Date found in DB and {} profile running So Updating: {} : {}\u001B[0m", commonConstant.getProfile(),CustomMataDataConstant.META_EXP_DATE_KEY,localDateTime);
            metaSettings.setDate(localDateTime);
            metaSettingsRepository.save(metaSettings);
            return true;
        }
        log.info("\u001B[32mExp Date Already Exists in DB, Skipping...\u001B[0m");
        return false;
    }

    @Transactional
    private boolean insertMissingPrivileges() {

        List<Privilege> privilegeList = privilegeRepository.findAll();

        List<PrivilegeListConfig.PrivilegeCsvRow> privilegeListFromFile = privilegeListConfig.getPrivilegeList();
        List<Privilege> newPrivilege = new ArrayList<>();
        List<Privilege> oldPrivilege = new ArrayList<>();
        boolean isPrivilegeAdded = false;
        for (PrivilegeListConfig.PrivilegeCsvRow privilege : privilegeListFromFile) {
            if (privilegeList.stream().noneMatch(p -> p.getName().equals(privilege.getName()))) {
                log.info("\u001B[33mPrivilege not found in DB, Adding: {},{},{}\u001B[0m", privilege.getName(), privilege.getCategory(), privilege.isSuperAdminOnly());
                Privilege privilegeObj = Privilege.builder()
                        .name(privilege.getName())
                        .privilegeCategory(privilegeCategoryMap.get(privilege.getCategory()))
                        .superAdminOnly(privilege.isSuperAdminOnly())
                        .build();
                privilegeRepository.save(privilegeObj);
                log.info("\u001B[32mif{}\u001B[0m",privilegeObj.getId());
                newPrivilege.add(privilegeObj);

                isPrivilegeAdded = true;
            }else{
                Privilege privilegeObj = privilegeRepository.findByName(privilege.getName());
                privilegeObj.setPrivilegeCategory(privilegeCategoryMap.get(privilege.getCategory()));
                privilegeObj.setSuperAdminOnly(privilege.isSuperAdminOnly());
                oldPrivilege.add(privilegeObj);
            }
        }
        if(oldPrivilege.size()>0){
            log.info("\u001B[32mUpdating Privileges Already Exists in DB\u001B[0m");
            privilegeRepository.saveAll(oldPrivilege);
        }
        if (isPrivilegeAdded) {
            log.info("\u001B[32mPrivileges Added Successfully\u001B[0m");
            Role role = roleRepository.findByName(UserConstant.SUPER_ADMIN);
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
        if (roleRepository.findByName(UserConstant.SUPER_ADMIN) == null) {
            log.info("\u001B[32mSuper Admin Role not found in DB, Added Successfully\u001B[32m");
            roleRepository.save(new Role(UserConstant.SUPER_ADMIN));
            return true;
        }
        log.info("\u001B[32mSuper Admin Role Already Exists in DB, Skipping...\u001B[32m");
        return false;
    }
    @Transactional
    private boolean insertTestUser() {
        Role role = roleRepository.findByName(UserConstant.SUPER_ADMIN);
        User user = userRepository.findByEmail(UserConstant.DEFAULT_USER_EMAIL);
        String password = UUID.randomUUID().toString();
        boolean isUserAdded = false;
        if (user == null) {
            log.info("Test User not found in DB, Adding");
            user = new User();
            user.setEmail(UserConstant.DEFAULT_USER_EMAIL);
            user.setPassword(UserConstant.PASSWORD_PREFIX + passwordEncoder.encode(password));
            user.setFirstName("Test");
            user.setLastName("Test");
            user.setRoles(List.of(role));
            user.setEnabled(true);
            userRepository.save(user);
            log.warn("\u001B[31m New Test User Added with \n{\n\tEmail:{}\n\tPassword: {}\n}\u001B[0m",
                    UserConstant.DEFAULT_USER_EMAIL, password);
            isUserAdded = true;
        } else if (user.getPassword() == null || user.getPassword().isEmpty()
                || user.getPassword().startsWith(UserConstant.PASSWORD_PREFIX)) {
            log.info("\u001B[33mTest User found in DB with default password, Changing Password\u001B[0m");
            user.setPassword(UserConstant.PASSWORD_PREFIX + passwordEncoder.encode(password));
            userRepository.save(user);
            log.warn("\u001B[31m Test User Updated with \n{\n\tEmail:{}\n\tPassword: {}\n}\u001B[0m",
                    UserConstant.DEFAULT_USER_EMAIL, password);
            isUserAdded = true;
        }
        if (isUserAdded) {
            log.warn("\u001B[31m Please change the password for the user: {}\u001B[0m", UserConstant.DEFAULT_USER_EMAIL);
        } else {
            log.info("\u001B[32m Test User already exists in DB with Custom Password, Skipping...\u001B[0m");
        }

        return isUserAdded;
    }

}
