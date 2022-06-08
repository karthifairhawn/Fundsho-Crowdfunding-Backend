package com.api.spring.boot.funsho.api.dto.requests;

import java.util.Date;

import lombok.Data;

@Data
public class updateRequestDTO {
    


    
    String additionalEdInfo; 


    // Contact Information
    String phoneNumber;
    String personalEmail;

    // Event Information
    String eventImageUrl;
    String eventTitle;
    Long amountRequired;
    Date deadLine;    
    String addtionalFilesUrl;
        
    String eventDescription;

    
}
