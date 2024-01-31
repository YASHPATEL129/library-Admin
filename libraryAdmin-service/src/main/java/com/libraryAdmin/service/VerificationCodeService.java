package com.libraryAdmin.service;

import com.libraryAdmin.model.params.CheckEmailCodeParam;
import com.libraryAdmin.model.params.SendEmailCodeParam;
import jakarta.servlet.http.HttpServletRequest;

public interface VerificationCodeService {

    void sendCode(SendEmailCodeParam param, HttpServletRequest request);

    void checkCode(CheckEmailCodeParam param, HttpServletRequest request);

    void removeCode(CheckEmailCodeParam checkCodePayload);
}
