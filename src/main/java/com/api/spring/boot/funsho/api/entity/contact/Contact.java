package com.api.spring.boot.funsho.api.entity.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Contact {
    
    @Id
    Long contactId;

    String name;
    String email;

    @Column(length =500)
    String data;

}
