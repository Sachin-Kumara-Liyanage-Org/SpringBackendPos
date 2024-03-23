package com.techbirdssolutions.springpos.repository;
import com.techbirdssolutions.springpos.entity.Privilege;
import com.techbirdssolutions.springpos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
    List<Privilege> findByNameIn(List<String> names);

    @Query("SELECT p FROM Privilege p WHERE p.name NOT IN :names")
    List<Privilege> findByNameNotIn(List<String> names);

}
