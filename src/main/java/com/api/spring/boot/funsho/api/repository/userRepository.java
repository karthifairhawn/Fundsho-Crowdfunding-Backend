package com.api.spring.boot.funsho.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.spring.boot.funsho.api.entity.users;

@Repository
public interface userRepository extends JpaRepository<users,Long> {

    public users findByEmail(String username);
    public users findByUserId(Long userId);
    
    public users findBySessionKey(String sessionKey);

    @Query(value = "SELECT fname FROM users WHERE user_id = ?1",nativeQuery = true)
    public String findFnameByUserId(Long userId);

    
}
