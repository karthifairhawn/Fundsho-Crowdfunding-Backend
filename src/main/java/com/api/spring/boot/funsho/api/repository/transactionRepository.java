package com.api.spring.boot.funsho.api.repository;

import com.api.spring.boot.funsho.api.entity.wallet.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface transactionRepository extends JpaRepository<transaction,Long>{

    @Modifying
    @Transactional
    @Query(value="delete from transaction where wallet_id = ?1",
    nativeQuery = true)
    void deleteByWalletId(Long walletId);

    List<transaction> findByRequestId(Long id);
    
    
    
}
