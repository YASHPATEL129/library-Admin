package com.libraryAdmin.service;

import com.libraryAdmin.model.params.*;
import com.libraryAdmin.pojo.response.AccountInfoResponse;
import com.libraryAdmin.pojo.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AccountAndService {

    void signUp(SignUpParam signUpParam , HttpServletRequest request ,HttpServletResponse response);

    AuthResponse signIn(SignInParam signInParam, HttpServletRequest request , HttpServletResponse response);

    AccountInfoResponse getAccountInfo(HttpServletRequest request );

    void changePassword(ChangePasswordParam changePasswordParam , HttpServletRequest request , HttpServletResponse response);

    AccountInfoResponse changeInfo(ChangeInfoParam changeInfoParam , HttpServletRequest request , HttpServletResponse response);

    void resetPassword(ResetPasswordParam resetPasswordParam , HttpServletRequest request, HttpServletResponse response);

    void superAdminChangePassword(SuperAdminResetPasswordParam param , HttpServletRequest request);
}
