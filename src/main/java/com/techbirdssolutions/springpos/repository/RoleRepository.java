package com.techbirdssolutions.springpos.repository;



import com.techbirdssolutions.springpos.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * This interface represents the RoleRepository in the application.
 * It extends JpaRepository to provide CRUD operations for the Role entity.
 * The interface is annotated with @Repository to indicate that it's a Spring Data JPA repository.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * This method is used to find a Role entity by its name.
     * @param name The name of the Role entity to find.
     * @return The Role entity if found, null otherwise.
     */
    Role findByName(String name);

}
