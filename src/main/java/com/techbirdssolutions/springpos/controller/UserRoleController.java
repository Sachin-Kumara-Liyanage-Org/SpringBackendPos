package com.techbirdssolutions.springpos.controller;


import com.techbirdssolutions.springpos.constant.CommonConstant;
import com.techbirdssolutions.springpos.entity.Role;
import com.techbirdssolutions.springpos.model.ResponseModel;
import com.techbirdssolutions.springpos.service.UserRoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-role")
@Slf4j
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;
    @PreAuthorize("hasAuthority('READ_UserRole')")
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/all")
    public ResponseEntity<ResponseModel> getAllUserRoles() {
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .data(userRoleService.getAllUserRoles())
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            log.warn("Error occurred while fetching user roles", e);
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAuthority('READ_UserRole')")
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getRoleById(@PathVariable Long id) {
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .data(userRoleService.getUserRoleById(id))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            log.warn("Error occurred while fetching user roles", e);
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAuthority('CREATE_UserRole')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/create")
    public ResponseEntity<ResponseModel> createUserRole(@RequestBody @Validated Role role) {
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.CREATED.value())
                    .success(true)
                    .message("User role created successfully")
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .data(userRoleService.saveUserRole(role))
                    .build(), HttpStatus.CREATED);

        } catch (Exception e) {
            log.warn("Error occurred", e);
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAuthority('UPDATE_UserRole')")
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/update")
    public ResponseEntity<ResponseModel> updateUserRole(@RequestBody @Validated Role role) {
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.ACCEPTED.value())
                    .success(true)
                    .message("User role updated successfully")
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .data(userRoleService.updateUserRole(role))
                    .build(), HttpStatus.ACCEPTED);

        } catch (Exception e) {
            log.warn("Error occurred", e);
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAuthority('DELETE_UserRole')")
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> deleteUserRole(@PathVariable Long id) {
        try{
            userRoleService.deleteUserRole(id);
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.ACCEPTED.value())
                    .success(true)
                    .message("User role deleted successfully")
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.ACCEPTED);

        } catch (Exception e) {
            log.warn("Error occurred", e);
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
