package com.libraryAdmin.model.params;

import lombok.Data;

@Data
public class SuperAdminResetPasswordParam {

    private String email;
    private String newPassword;
}
