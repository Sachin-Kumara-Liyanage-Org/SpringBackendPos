package com.techbirdssolutions.springpos.repository;

import com.techbirdssolutions.springpos.config.DefaultDataLoad;
import com.techbirdssolutions.springpos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/**
 * This interface represents the UserRepository in the application.
 * It extends JpaRepository to provide CRUD operations for the User entity.
 * The interface is annotated with @Repository to indicate that it's a Spring Data JPA repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * This method is used to find a User entity by its email.
     * @param email The email of the User entity to find.
     * @return The User entity if found, null otherwise.
     */
    User findByEmail(String email);
    /**
     * This method is used to find a User entity by its refresh token.
     * @param refreshToken The refresh token of the User entity to find.
     * @return The User entity if found, null otherwise.
     */
    User findByRefreshToken(String refreshToken);
    /**
     * This method is used to check if a User entity with a specific email and admin role exists.
     * It uses a custom query defined with the @Query annotation.
     * @param email The email of the User entity to check.
     * @param roleName The name of the role to check.
     * @return true if the User entity exists and has the admin role, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.roles r WHERE u.email = :email AND r.name = :roleName")
    boolean existsByEmailAndAdminRole(@Param("email") String email, @Param("roleName") String roleName);
}