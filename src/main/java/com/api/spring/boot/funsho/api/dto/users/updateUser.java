package com.api.spring.boot.funsho.api.dto.users;

import java.util.Date;

import lombok.Data;

@Data
public class updateUser {    
    String fname;
    String lname;
    Date dob;
    String email;
    String phNumber;
    String sessionKey;
    String password;
    String username;
    Boolean changePassword;
}
