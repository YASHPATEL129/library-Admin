package com.libraryAdmin.service;

import com.libraryAdmin.entity.Admin;
import com.libraryAdmin.model.params.*;
import com.libraryAdmin.pojo.response.AccountInfoResponse;
import com.libraryAdmin.pojo.response.AuthResponse;
import com.libraryAdmin.pojo.response.IsSuperAdminResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface AccountAndService {

    void signUp(CreateAdminParam createAdminParam, HttpServletRequest request , HttpServletResponse response);

    AuthResponse signIn(SignInParam signInParam, HttpServletRequest request , HttpServletResponse response);

    AccountInfoResponse getAccountInfo(HttpServletRequest request );

    void changePassword(ChangePasswordParam changePasswordParam , HttpServletRequest request , HttpServletResponse response);

    AccountInfoResponse changeInfo(ChangeInfoParam changeInfoParam , HttpServletRequest request , HttpServletResponse response);

    void resetPassword(ResetPasswordParam resetPasswordParam , HttpServletRequest request, HttpServletResponse response);

    void superAdminChangePassword(SuperAdminResetPasswordParam param , HttpServletRequest request);

    List<Admin> getAdminList(Boolean isSuperAdmin, HttpServletRequest request , HttpServletResponse response);

    IsSuperAdminResponse getIsSuperAdmin(HttpServletRequest request );
}
