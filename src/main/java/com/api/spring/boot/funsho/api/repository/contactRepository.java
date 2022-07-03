package com.api.spring.boot.funsho.api.repository;

import org.springframework.stereotype.Repository;
import com.api.spring.boot.funsho.api.entity.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;



@Repository
public interface contactRepository extends JpaRepository<Contact,Long>{

    
    

}
