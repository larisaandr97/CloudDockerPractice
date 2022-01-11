package com.amigoscode.customer.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.amigoscode.customer.entity.Pattern.EMAIL_ADDRESS;

@Data
public class CustomerRequest {

    @NotBlank
    @Length(min = 8, max = 20)
    private String username;

    @NotBlank
    @Length(max = 100)
    private String firstName;

    @NotBlank
    @Length(max = 100)
    private String lastName;

    @NotBlank
    @Length(min = 8, max = 100)
    private String password;

    @NotBlank
    @Pattern(regexp = EMAIL_ADDRESS)
    @Length(max = 100)
    private String email;

    public CustomerRequest(String username, String firstName, String lastName, String password, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }
}
