package com.api.spring.boot.funsho.api.entity.contact;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long contactId;

    String name;
    String email;

    @Column(length =500)
    String data;

    Date timestamp;

}
