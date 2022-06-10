package com.api.spring.boot.funsho.api.dto.users;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class singleDonation {
    
    private Long donorId;
    private String donorName;
    private String description;
    private Long amount;
    private Date dateTime;

}
