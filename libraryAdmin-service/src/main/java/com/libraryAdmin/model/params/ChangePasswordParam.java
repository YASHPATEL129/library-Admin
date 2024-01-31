package com.libraryAdmin.model.params;

import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordParam {

    private String currentPassword;

    private String newPassword;
}
