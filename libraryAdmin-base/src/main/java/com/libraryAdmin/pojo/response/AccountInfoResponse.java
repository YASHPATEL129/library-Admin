package com.libraryAdmin.pojo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountInfoResponse {

    String userName;
    String firstName;
    String lastName;
    String email;
    String contact;
    Boolean isActive;
    Boolean isSuperAdmin;
    Long createdBy;
}
