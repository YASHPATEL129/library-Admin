package com.libraryAdmin.pojo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {

    String accessToken;
    String refreshToken;
    String deviceToken;
    String email;
    String userName;
    String firstName;
    String lastName;
    String contact;
    Boolean isSuperAdmin;
}
