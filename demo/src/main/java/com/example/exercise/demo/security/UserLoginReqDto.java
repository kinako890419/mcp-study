package com.example.exercise.demo.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginReqDto {

    @JsonProperty("user_mail")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 50, message = "Email needs to be less than 50 characters")
    @Email(message = "email must be valid")
    private String userMail;

    @JsonProperty("user_password")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
    private String userPassword;

}
