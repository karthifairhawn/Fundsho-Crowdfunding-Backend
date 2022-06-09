package com.api.spring.boot.funsho.api.dto.requests;

import lombok.Data;

@Data
public class donateDTO {
    Long donationAmount;    
    String donationType;
    String donationDescription;
    String donationDateTime;
    String donationStatus;    // pending, approved, rejected
}
