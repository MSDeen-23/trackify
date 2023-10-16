package com.beworkerbee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Index;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Indexed;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestUser {

    @NotBlank(message = "Firstname is mandatory")
    @JsonAlias({"first_name","firstName"})
    private String firstName;

    @NotBlank(message = "Lastname is mandatory")
    @JsonAlias({"last_name","lastName"})
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @JsonProperty("password")
//    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;<>,.?~]{9,}$")
    private String password;

}
