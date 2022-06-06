package com.api.spring.boot.funsho.api.repository;

import com.api.spring.boot.funsho.api.entity.wallet.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface transactionRepository extends JpaRepository<transaction,Long>{

    @Query("delete from transaction where wallet_id = ?1")
    void deleteByWalletId(Long walletId);
    
}
