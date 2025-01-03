package com.urssa.urssaAppPressing.v2.company.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDto {

    private String name;

    private String regNo;

    private String email;

    private Long phone;

    private String postalNo;

    private String street;

    private String city;

    private String Country;
}
