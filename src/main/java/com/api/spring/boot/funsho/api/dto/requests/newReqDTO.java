package com.api.spring.boot.funsho.api.dto.requests;

import java.util.Date;

import lombok.Data;


@Data
public class newReqDTO {

    // Personal Information 
    String fname;
    String lname;
    String gender;        
    String background;    
    Date dateOfBirth;

    // Educational Information
    String institutionName; 
    String studyProgram;
    String institutePlace;  

    
    String additionalEdInfo; 


    // Contact Information
    Long phoneNumber;
    String address;
    String city;
    String pinCode;
    String stateRegion;
    String personalEmail;

    // Event Information
    String eventImageUrl;
    String eventTitle;
    Long amountRequired;
    Date deadLine;    
    String addtionalFilesUrl;
        
    String eventDescription;
    
}
