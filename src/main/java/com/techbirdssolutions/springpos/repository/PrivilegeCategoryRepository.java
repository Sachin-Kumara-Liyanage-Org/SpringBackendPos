package com.techbirdssolutions.springpos.repository;

import com.techbirdssolutions.springpos.entity.PrivilegeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeCategoryRepository extends JpaRepository<PrivilegeCategory, Long> {
    PrivilegeCategory findByName(String name);

    @Query("SELECT pc FROM PrivilegeCategory pc WHERE pc.name NOT IN :names")
    List<PrivilegeCategory> findByNameNotIn(List<String> names);
}
