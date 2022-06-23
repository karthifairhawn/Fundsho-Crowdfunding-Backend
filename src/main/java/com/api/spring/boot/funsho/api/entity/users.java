package com.api.spring.boot.funsho.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.api.spring.boot.funsho.api.dto.users.createUser;
import com.api.spring.boot.funsho.api.entity.wallet.wallet;
import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("userFilter")
public class users {

    public users(createUser newUser) {
        this.fname = newUser.getFname();
        this.lname = newUser.getLname();
        this.email = newUser.getEmail();
        this.password = newUser.getPassword();
        this.phNumber = newUser.getPhNumber();
        this.dob = newUser.getDob();
        this.username = newUser.getUsername();    
        this.avatarUrl = newUser.getAvatarUrl();   
        this.bio = newUser.getBio(); 
    }


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long userId;

    String fname;
    String lname;
    String avatarUrl;
    Date dob;
    String email;
    String phNumber;
    String sessionKey;
    String password;
    String username;

    @Column(length = 500)
    String bio;

    Long role;
    

    @OneToOne(
        mappedBy = "user"
    )

    private wallet Wallet;
}
