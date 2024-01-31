package com.libraryAdmin.model.params;

import com.libraryAdmin.validators.ValidEmail;
import com.libraryAdmin.validators.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpParam {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String contact;
}
