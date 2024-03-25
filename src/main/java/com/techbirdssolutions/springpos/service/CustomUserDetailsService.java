package com.techbirdssolutions.springpos.service;

import com.techbirdssolutions.springpos.config.DefaultDataLoad;
import com.techbirdssolutions.springpos.constant.UserConstant;
import com.techbirdssolutions.springpos.entity.Privilege;
import com.techbirdssolutions.springpos.entity.Role;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.repository.RoleRepository;
import com.techbirdssolutions.springpos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * This service class is responsible for handling user details.
 * It implements UserDetailsService interface from Spring Security to provide user details based on username.
 * It is annotated with @Service to indicate that it's a Spring Service.
 * It is also annotated with @Transactional to ensure that all methods within this class are associated with a transaction.
 */
@Service("userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    /**
     * This method loads user details based on the username (in this case, email).
     * It throws UsernameNotFoundException if the user is not found.
     * @param email The email of the user.
     * @return UserDetails object containing user's details.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Arrays.asList(
                            roleRepository.findByName("ROLE_USER"))));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword()==null?null:user.getPassword().replaceAll(UserConstant.PASSWORD_PREFIX,""), user.isEnabled(), true, true,
                true, getAuthorities(user.getRoles()));
    }
    /**
     * This method converts a collection of Role entities into a collection of GrantedAuthority.
     * @param roles The collection of Role entities.
     * @return A collection of GrantedAuthority.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    /**
     * This method extracts privileges from a collection of Role entities.
     * @param roles The collection of Role entities.
     * @return A list of privileges.
     */
    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }
    /**
     * This method generates a map containing the privileges and roles of a given collection of Role entities.
     * The outer map's key is a string which can be either "privileges" or "roles", and the value is another map.
     * The inner map's key is the ID of the privilege or role, and the value is the name of the privilege or role.
     *
     * @param email The email of the user.
     * @return A map containing the privileges and roles of the given roles.
     */
    @Transactional
    public Map<String,Map<Long,String>> getPrivilegesAndRolesMap(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        Collection<Role> roles = user.getRoles();
        Map<Long,String> privileges = new HashMap<>();
        Map<Long,String> userRoles = new HashMap<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            userRoles.put(role.getId(),role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.put(item.getId(),item.getName());
        }
        Map<String,Map<Long,String> > map = new HashMap<>();
        map.put("privileges",privileges);
        map.put("roles",userRoles);
        return map;
    }
    /**
     * This method converts a list of privileges into a list of GrantedAuthority.
     * @param privileges The list of privileges.
     * @return A list of GrantedAuthority.
     */
    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}