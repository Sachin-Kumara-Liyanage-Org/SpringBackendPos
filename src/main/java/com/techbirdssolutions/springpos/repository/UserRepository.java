package com.techbirdssolutions.springpos.repository;

import com.techbirdssolutions.springpos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
    User findByRefreshToken(String refreshToken);
}