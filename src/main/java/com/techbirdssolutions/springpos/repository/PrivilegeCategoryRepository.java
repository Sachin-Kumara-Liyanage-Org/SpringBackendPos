package com.techbirdssolutions.springpos.repository;

import com.techbirdssolutions.springpos.entity.PrivilegeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * This interface represents the PrivilegeCategoryRepository in the application.
 * It extends JpaRepository to provide CRUD operations for the PrivilegeCategory entity.
 * The interface is annotated with @Repository to indicate that it's a Spring Data JPA repository.
 */
@Repository
public interface PrivilegeCategoryRepository extends JpaRepository<PrivilegeCategory, Long> {
    /**
     * This method is used to find a PrivilegeCategory entity by its name.
     * @param name The name of the PrivilegeCategory entity to find.
     * @return The PrivilegeCategory entity if found, null otherwise.
     */
    PrivilegeCategory findByName(String name);
    /**
     * This method is used to find PrivilegeCategory entities that do not have any of the names provided.
     * It uses a custom query defined with the @Query annotation.
     * @param names The list of names to exclude in the search.
     * @return A list of PrivilegeCategory entities that do not have any of the provided names.
     */
    @Query("SELECT pc FROM PrivilegeCategory pc WHERE pc.name NOT IN :names")
    List<PrivilegeCategory> findByNameNotIn(List<String> names);
}
