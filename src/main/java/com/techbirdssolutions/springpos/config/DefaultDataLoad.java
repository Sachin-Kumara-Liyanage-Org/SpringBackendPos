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
/**
 * DefaultDataLoad is a component class that is responsible for loading default data into the application.
 * It is annotated with @Component to indicate that an instance of this class will be created at startup.
 * It is also annotated with @Slf4j to enable logging.
 * The class is marked as @Transactional, meaning that the methods within this class will be wrapped in a transaction.
 */
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
    /**
     * This method is responsible for running the default data load process.
     * It logs the success or failure of each step in the process.
     * If an exception occurs during the process, it is caught and logged, and the method returns false.
     *
     * @return a boolean indicating whether the default data load process was successful
     */
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
    /**
     * This method is used to remove old privileges and privilege categories from the database.
     * It first retrieves a list of privileges and privilege categories that are not in the configured list of privileges and categories.
     * If there are any privileges in the retrieved list, it logs the number of privileges to be removed.
     * For each privilege in the list, it removes the privilege from each role that has it, and then removes the privilege itself.
     * If there are any privilege categories in the retrieved list, it logs the number of categories to be removed and then removes them.
     * The method returns true if any privileges or categories were removed, and false otherwise.
     *
     * @return a boolean indicating whether any privileges or categories were removed
     */
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
    /**
     * This method is used to insert missing privilege categories into the database.
     * It first retrieves a list of all privilege categories from the database.
     * Then, for each category in the configured list of privilege categories, it checks whether the category exists in the retrieved list.
     * If a category does not exist in the retrieved list, it logs the category name, creates a new PrivilegeCategory object with the category name,
     * saves the new PrivilegeCategory object to the database, adds the new PrivilegeCategory object to the privilegeCategoryMap, and sets isPrivilegeCategoryAdded to true.
     * If a category does exist in the retrieved list, it retrieves the PrivilegeCategory object with the category name from the database and adds it to the privilegeCategoryMap.
     * The method returns isPrivilegeCategoryAdded, which indicates whether any privilege categories were added.
     *
     * @return a boolean indicating whether any privilege categories were added
     */
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
    /**
     * This method is used to insert or update the expiration date in the database.
     * It first retrieves the MetaSettings object with the key CustomMataDataConstant.META_EXP_DATE_KEY from the database.
     * If the MetaSettings object does not exist, it creates a new LocalDateTime object that is 30 days from now, logs the key and the LocalDateTime object,
     * creates a new MetaSettings object with the key, the LocalDateTime object, and the type MetadataTypes.DATE_TIME, saves the new MetaSettings object to the database, and returns true.
     * If the MetaSettings object does exist and the application is running in local mode, it creates a new LocalDateTime object that is 30 days from now, logs the profile, the key, and the LocalDateTime object,
     * sets the date of the MetaSettings object to the LocalDateTime object, saves the MetaSettings object to the database, and returns true.
     * If the MetaSettings object does exist and the application is not running in local mode, it logs that the expiration date already exists in the database and returns false.
     *
     * @return a boolean indicating whether the expiration date was inserted or updated
     */
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
    /**
     * This method is used to insert missing privileges into the database.
     * It first retrieves a list of all privileges from the database.
     * Then, for each privilege in the configured list of privileges, it checks whether the privilege exists in the retrieved list.
     * If a privilege does not exist in the retrieved list, it logs the privilege name, category, and superAdminOnly status, creates a new Privilege object with the privilege name, category, and superAdminOnly status,
     * saves the new Privilege object to the database, adds the new Privilege object to the newPrivilege list, and sets isPrivilegeAdded to true.
     * If a privilege does exist in the retrieved list, it retrieves the Privilege object with the privilege name from the database, sets the privilege category and superAdminOnly status of the Privilege object, and adds the Privilege object to the oldPrivilege list.
     * If there are any privileges in the oldPrivilege list, it logs that the privileges already exist in the database and updates the privileges in the database.
     * If isPrivilegeAdded is true, it logs that the privileges were added successfully, retrieves the Role object with the name UserConstant.SUPER_ADMIN from the database, adds the privileges in the newPrivilege list to the Role object, saves the Role object to the database, and logs that the privileges were added to the role successfully.
     * If isPrivilegeAdded is false, it logs that the privileges already exist in the database and skips the addition of the privileges.
     * The method returns isPrivilegeAdded, which indicates whether any privileges were added.
     *
     * @return a boolean indicating whether any privileges were added
     */
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
    /**
     * This method is used to insert the Super Admin role into the database.
     * It first checks whether the Role object with the name UserConstant.SUPER_ADMIN exists in the database.
     * If the Role object does not exist, it logs that the Super Admin role was not found in the database and was added successfully,
     * creates a new Role object with the name UserConstant.SUPER_ADMIN, saves the new Role object to the database, and returns true.
     * If the Role object does exist, it logs that the Super Admin role already exists in the database and skips the addition of the role, and returns false.
     *
     * @return a boolean indicating whether the Super Admin role was added
     */
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

    /**
     * This method is used to insert a test user into the database.
     * It first retrieves the Role object with the name UserConstant.SUPER_ADMIN and the User object with the email UserConstant.DEFAULT_USER_EMAIL from the database.
     * If the User object does not exist, it logs that the test user was not found in the database and is being added, creates a new User object with the email UserConstant.
     * DEFAULT_USER_EMAIL, a randomly generated password, the first name "Test", the last name "Test", the Role object, and the enabled status set to true,
     * saves the new User object to the database, logs the email and password of the new user, and sets isUserAdded to true.
     * If the User object does exist and the password of the User object is null, empty, or starts with UserConstant.PASSWORD_PREFIX,
     * it logs that the test user was found in the database with the default password and is changing the password,
     * sets the password of the User object to a randomly generated password, saves the User object to the database, logs the email and password of the user,
     * and sets isUserAdded to true.
     * If isUserAdded is true, it logs a warning to change the password for the user with the email UserConstant.DEFAULT_USER_EMAIL.
     * If isUserAdded is false, it logs that the test user already exists in the database with a custom password and skips the addition of the user.
     * The method returns isUserAdded, which indicates whether the test user was added or updated.
     *
     * @return a boolean indicating whether the test user was added or updated
     */
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
