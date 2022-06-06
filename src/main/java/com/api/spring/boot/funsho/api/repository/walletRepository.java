package com.api.spring.boot.funsho.api.repository;

import com.api.spring.boot.funsho.api.entity.wallet.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin
public interface walletRepository extends JpaRepository<wallet,Long>{

    @Query("select w from wallet w where w.user.id = ?1")
    wallet findByUserId(long userId);

}
