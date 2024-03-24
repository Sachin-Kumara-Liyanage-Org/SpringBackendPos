package com.techbirdssolutions.springpos.repository;
import com.techbirdssolutions.springpos.entity.Privilege;
import com.techbirdssolutions.springpos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * This interface represents the PrivilegeRepository in the application.
 * It extends JpaRepository to provide CRUD operations for the Privilege entity.
 * The interface is annotated with @Repository to indicate that it's a Spring Data JPA repository.
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    /**
     * This method is used to find a Privilege entity by its name.
     * @param name The name of the Privilege entity to find.
     * @return The Privilege entity if found, null otherwise.
     */
    Privilege findByName(String name);

    /**
     * This method is used to find Privilege entities that have any of the names provided.
     * @param names The list of names to include in the search.
     * @return A list of Privilege entities that have any of the provided names.
     */
    List<Privilege> findByNameIn(List<String> names);
    /**
     * This method is used to find Privilege entities that do not have any of the names provided.
     * It uses a custom query defined with the @Query annotation.
     * @param names The list of names to exclude in the search.
     * @return A list of Privilege entities that do not have any of the provided names.
     */
    @Query("SELECT p FROM Privilege p WHERE p.name NOT IN :names")
    List<Privilege> findByNameNotIn(List<String> names);

}
