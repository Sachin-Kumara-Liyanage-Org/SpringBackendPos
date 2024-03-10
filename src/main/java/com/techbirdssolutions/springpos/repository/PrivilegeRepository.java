package com.techbirdssolutions.springpos.repository;
import com.techbirdssolutions.springpos.entity.Privilege;
import com.techbirdssolutions.springpos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
