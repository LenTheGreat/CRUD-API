package com.metrobank.testapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AccountsDTO {

    private Long accountId;
    private String mobileNumber;
    private String emailAddress;

}
