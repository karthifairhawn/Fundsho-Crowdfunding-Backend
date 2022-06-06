package com.api.spring.boot.funsho.api.repository;

import com.api.spring.boot.funsho.api.entity.wallet.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface transactionRepository extends JpaRepository<transaction,Long>{

    @Modifying
    @Query(value="delete from transaction where wallet_id = ?1",
    nativeQuery = true)
    void deleteByWalletId(Long walletId);
    
}
