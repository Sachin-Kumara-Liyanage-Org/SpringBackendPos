package com.techbirdssolutions.springpos.repository;

import com.techbirdssolutions.springpos.entity.MetaSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaSettingsRepository extends JpaRepository<MetaSettings, Long> {
    MetaSettings findByName(String name);
}
