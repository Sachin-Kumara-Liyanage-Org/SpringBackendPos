package com.techbirdssolutions.springpos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techbirdssolutions.springpos.entity.Privilege;
import com.techbirdssolutions.springpos.entity.Role;
import com.techbirdssolutions.springpos.exception.UserException;
import com.techbirdssolutions.springpos.repository.PrivilegeRepository;
import com.techbirdssolutions.springpos.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;



    public List<Role> getAllUserRoles() throws JsonProcessingException {
        return roleRepository.findAll();
    }

    public Role getUserRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role saveUserRole(Role role) {
        role.setId(null);
        List<Long> privilegeIds = role.getPrivileges().stream().map(privilege -> privilege.getId()).toList();
        List<Privilege> privileges = privilegeRepository.findAllById(privilegeIds);
        role.setPrivileges(privileges);
        return roleRepository.save(role);
    }

    public Role updateUserRole(Role role) {
        Role existingRole = roleRepository.findById(role.getId()).orElse(null);
        if (existingRole == null) {
            return null;
        }
        existingRole.setName(role.getName());
        List<Long> privilegeIds = role.getPrivileges().stream().map(privilege -> privilege.getId()).toList();
        List<Privilege> privileges = privilegeRepository.findAllById(privilegeIds);
        existingRole.setPrivileges(privileges);
        return roleRepository.save(existingRole);
    }

    public void deleteUserRole(Long id) throws UserException {
        Role role = roleRepository.findById(id).orElse(null);
        if(role == null) {
            throw new UserException("Role not found");
        }
        role.getPrivileges().clear();
        roleRepository.save(role);
        roleRepository.deleteById(id);
    }


}
