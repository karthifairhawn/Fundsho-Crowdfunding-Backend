package com.api.spring.boot.funsho.api.dto.users;

import java.util.Date;

import lombok.Data;

@Data
public class createUser{    
    String fname;
    String lname;
    Date dob;
    String email;
    String phNumber;    
    String password;
    String username; 
    String avatarUrl;   
}
