package com.libraryAdmin.model.params;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeInfoParam {


    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String contact;
}
