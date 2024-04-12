package com.libraryAdmin.service.Impl;

import com.libraryAdmin.consts.AppConfigs;
import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Admin;
import com.libraryAdmin.enums.DeviceTypes;
import com.libraryAdmin.exception.ForbiddenException;
import com.libraryAdmin.exception.InvalidCredentialsException;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.hashRepository.UserSessionTrackerRepo;
import com.libraryAdmin.helper.SystemHelper;
import com.libraryAdmin.model.hashes.UserSessionTracker;
import com.libraryAdmin.model.params.*;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.pojo.response.AccountInfoResponse;
import com.libraryAdmin.pojo.response.AuthResponse;
import com.libraryAdmin.pojo.response.IsSuperAdminResponse;
import com.libraryAdmin.repository.AdminRepository;
import com.libraryAdmin.service.AccountAndService;
import com.libraryAdmin.service.VerificationCodeService;
import com.libraryAdmin.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountAndServiceImpl implements AccountAndService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;


    @Autowired
    AuthenticationManager authenticateManager;

    @Autowired
    CurrentSession currentSession;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserSessionTrackerRepo userSessionTrackerRepo;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Override
    public void signUp(SignUpParam signUpParam, HttpServletRequest request, HttpServletResponse response) {

        Admin admin = new Admin();
        admin.setFirstName(signUpParam.getFirstName());
        admin.setLastName(signUpParam.getLastName());
        admin.setEmail(signUpParam.getEmail());
        admin.setContact(signUpParam.getContact());
        admin.setPassword(passwordEncoder.encode(signUpParam.getPassword()));
        admin.setCreatedBy(currentSession.getId());
        String userName = SystemHelper.generateUsername();
        admin.setUserName(userName);
        adminRepository.save(admin);
    }

    @Override
    public AuthResponse signIn(SignInParam signInParam, HttpServletRequest request, HttpServletResponse response) {
        try{
            authenticateManager.authenticate(new UsernamePasswordAuthenticationToken(signInParam.getEmail(), signInParam.getPassword()));
        } catch (AuthenticationException ex){
            throw new InvalidCredentialsException(Message.INCORRECT_ACCOUNT_OR_PASSWORD, ErrorKeys.INCORRECT_ACCOUNT_OR_PASSWORD);
        }
        AuthResponse authResponse = jwtUtil.generateToken(signInParam.getEmail(), request, response);
        Admin admin = adminRepository.findByEmail(signInParam.getEmail());
        authResponse.setUserName(admin.getUserName());
        authResponse.setEmail(admin.getEmail());
        authResponse.setFirstName(admin.getFirstName());
        authResponse.setLastName(admin.getLastName());
        authResponse.setContact(admin.getContact());
        authResponse.setIsSuperAdmin(admin.getIsSuperAdmin());
        configureSession(signInParam.getEmail(), authResponse.getDeviceToken(), request.getHeader(AppConfigs.DEVICE_TYPE_NAME));
        return authResponse;
    }

    @Override
    public AccountInfoResponse getAccountInfo(HttpServletRequest request) {
        Admin admin = adminRepository.findByEmail(currentSession.getEmail());
        AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
        accountInfoResponse.setUserName(admin.getUserName());
        accountInfoResponse.setFirstName(admin.getFirstName());
        accountInfoResponse.setLastName(admin.getLastName());
        accountInfoResponse.setEmail(admin.getEmail());
        accountInfoResponse.setContact(admin.getContact());
        accountInfoResponse.setCreatedBy(admin.getCreatedBy());
        accountInfoResponse.setIsActive(admin.getIsActive());
        accountInfoResponse.setIsSuperAdmin(admin.getIsSuperAdmin());
        return accountInfoResponse;
    }

    @Override
    public void changePassword(ChangePasswordParam changePasswordParam, HttpServletRequest request, HttpServletResponse response) {
        Admin user = adminRepository.findByEmail(currentSession.getEmail());
        if (StringUtils.equals(changePasswordParam.getCurrentPassword(), changePasswordParam.getNewPassword())) {
            throw new ForbiddenException(Message.SAME_PASSWORD, ErrorKeys.SAME_PASSWORD);
        }
        validateCurrentPassword(changePasswordParam.getCurrentPassword(), user.getPassword());
        try {
            user.setPassword(passwordEncoder.encode(changePasswordParam.getNewPassword()));
            adminRepository.save(user);
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public AccountInfoResponse changeInfo(ChangeInfoParam changeInfoParam, HttpServletRequest request, HttpServletResponse response) {
        Admin admin = adminRepository.findByEmail((currentSession.getEmail()));
        if (admin == null) {
            throw new ForbiddenException(Message.INCORRECT_ACCOUNT_OR_PASSWORD , ErrorKeys.INCORRECT_ACCOUNT_OR_PASSWORD);
        }
        try {
            admin.setFirstName(changeInfoParam.getFirstName());
            admin.setLastName(changeInfoParam.getLastName());
            admin.setContact(changeInfoParam.getContact());
            adminRepository.save(admin);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        Admin admin1 = adminRepository.findByEmail(currentSession.getEmail());
        AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
        accountInfoResponse.setUserName(admin1.getUserName());
        accountInfoResponse.setFirstName(admin1.getFirstName());
        accountInfoResponse.setLastName(admin1.getLastName());
        accountInfoResponse.setEmail(admin1.getEmail());
        accountInfoResponse.setContact(admin.getContact());
        return accountInfoResponse;
    }

    @Override
    public void resetPassword(ResetPasswordParam resetPasswordParam, HttpServletRequest request, HttpServletResponse response) {
        CheckEmailCodeParam checkCodePayLoad = new CheckEmailCodeParam().setEmail(resetPasswordParam.getEmail()).setCode(resetPasswordParam.getCode());
        verificationCodeService.checkCode(checkCodePayLoad, request);
        Admin admin = adminRepository.findByEmail(resetPasswordParam.getEmail());
        if (admin != null) {
            if (passwordEncoder.matches(resetPasswordParam.getPassword(), admin.getPassword())) {
                throw new ForbiddenException(Message.SAME_PASSWORD, ErrorKeys.SAME_PASSWORD);
            }

            admin.setPassword(passwordEncoder.encode(resetPasswordParam.getPassword()));
            adminRepository.save(admin);
            verificationCodeService.removeCode(checkCodePayLoad);
            return;
        }
        throw new ForbiddenException(Message.PASSWORD_RESET_FAILED, ErrorKeys.PASSWORD_RESET_FAILED);
    }

    @Override
    public void superAdminChangePassword(SuperAdminResetPasswordParam param, HttpServletRequest request) {
        if (!currentSession.getIsSuperAdmin())
            throw new ForbiddenException(Message.UNAUTHORIZED,ErrorKeys.UNAUTHORIZED);
        Admin admin = adminRepository.findByEmail(currentSession.getEmail());
        if (admin == null) {
            throw new ForbiddenException(Message.INCORRECT_ACCOUNT_OR_PASSWORD , ErrorKeys.INCORRECT_ACCOUNT_OR_PASSWORD);
        }
        try {
            if (admin.getIsSuperAdmin()) {
                String otherAdmin = param.getEmail();
                Admin admin1 = adminRepository.findByEmail(otherAdmin);
                if (otherAdmin != null && !admin1.getIsSuperAdmin()) {
                    admin1.setPassword(passwordEncoder.encode(param.getNewPassword()));
                    adminRepository.save(admin1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Admin> getAdminList(Boolean isSuperAdmin, HttpServletRequest request, HttpServletResponse response) {
        if (!currentSession.getIsSuperAdmin())
            throw new ForbiddenException(Message.UNAUTHORIZED,ErrorKeys.UNAUTHORIZED);
        return adminRepository.findByIsSuperAdmin(isSuperAdmin);
    }

    @Override
    public IsSuperAdminResponse getIsSuperAdmin(HttpServletRequest request) {
        Admin admin = adminRepository.findByEmail(currentSession.getEmail());
        IsSuperAdminResponse isSuperAdminResponse = new IsSuperAdminResponse();
        isSuperAdminResponse.setIsSuperAdmin(admin.getIsSuperAdmin());
        return isSuperAdminResponse;
    }

    private void configureSession(String email , String deviceVerificationToken , String deviceType){
        UserSessionTracker session = userSessionTrackerRepo.getSession(email , deviceType);
        session = new UserSessionTracker();
        session.setUserEmail(email);
        session.setVerificationId(deviceVerificationToken);
        try {
            session.setDeviceType(DeviceTypes.valueOf(deviceType));
        } catch (Exception e) {
            throw new ValidationException();
        }
        userSessionTrackerRepo.save(session);
    }

    private void validateCurrentPassword(String rawPassword , String password) {
        if (!passwordEncoder.matches(rawPassword, password)) {
            throw new ForbiddenException(Message.INCORRECT_CURRENT_PASSWORD, ErrorKeys.INCORRECT_CURRENT_PASSWORD);
        }
    }
}
