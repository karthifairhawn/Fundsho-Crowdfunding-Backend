package com.api.spring.boot.funsho.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.spring.boot.funsho.api.entity.Files;



@Repository
public interface filesRepository extends JpaRepository<Files, String> {
}