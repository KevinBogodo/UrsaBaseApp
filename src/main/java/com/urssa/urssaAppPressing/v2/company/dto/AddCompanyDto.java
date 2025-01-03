package com.urssa.urssaAppPressing.v2.company.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCompanyDto {

    @NotBlank(message = "name_cannot_be_blank")
    private String name;

    private String regNo;

    @NotBlank(message = "email_cannot_be_blank")
    private String email;

    @NotBlank(message = "phone_cannot_be_blank")
    private Long phone;

    private String postalNo;

    private String street;

    @NotBlank(message = "city_cannot_be_blank")
    private String city;

    @NotBlank(message = "country_cannot_be_blank")
    private String Country;
}
