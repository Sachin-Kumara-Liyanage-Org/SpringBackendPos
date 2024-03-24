package com.techbirdssolutions.springpos.repository;

import com.techbirdssolutions.springpos.entity.MetaSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * This interface represents the MetaSettingsRepository in the application.
 * It extends JpaRepository to provide CRUD operations for the MetaSettings entity.
 * The interface is annotated with @Repository to indicate that it's a Spring Data JPA repository.
 */
@Repository
public interface MetaSettingsRepository extends JpaRepository<MetaSettings, Long> {
    /**
     * This method is used to find a MetaSettings entity by its name.
     * @param name The name of the MetaSettings entity to find.
     * @return The MetaSettings entity if found, null otherwise.
     */
    MetaSettings findByName(String name);
}
