package com.libraryAdmin.Controllers;

import com.libraryAdmin.consts.AppConfigs;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.model.params.*;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.AccountAndService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.libraryAdmin.consts.Message.PASSWORD_RESET_SUCCESSFUL;

@RestController
@RequestMapping("/v1")
public class AccountAndServiceController {

    @Autowired
    private AccountAndService accountAndService;



        @PostMapping("/create/super/admin")
    public ResponseEntity<Success<?>> createAdmin(@RequestBody @Validated CreateAdminParam createAdminParam,
                                             BindingResult error,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {

        if (error.hasErrors()) {
            throw new ValidationException(error);
        }

        accountAndService.signUp(createAdminParam, request, response);
        Success<?> success = new Success<>();
        success.setMessageCode(Message.CREATE_SUCCESSFUL);

        ResponseEntity<Success<?>> responseEntity = new ResponseEntity<>(success, HttpStatus.CREATED);
        return responseEntity;
    }

    @PostMapping("/signIn")
    public ResponseEntity<Success<?>> signIn(@RequestBody SignInParam signInParam,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        Object data = accountAndService.signIn(signInParam, request, response);
        Success<?> success = new Success<>();
        success.setMessageCode(Message.LOGIN_SUCCESSFUL);
        success.setData(data);
        ResponseEntity<Success<?>> responseEntity = new ResponseEntity<>(success, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "/account-info")
    public ResponseEntity<Success<?>> getAccountInfo(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(accountAndService.getAccountInfo(request));
        success.setMessageCode(Message.DATA_GET_SUCCESSFUL);
        return respBuilder.body(success);
    }


    @PutMapping(value = "/changePassword")
    public ResponseEntity<Success<?>> changePassword(@RequestBody ChangePasswordParam changePasswordParam, BindingResult error, HttpServletRequest request, HttpServletResponse response){
        String requestType = request.getHeader(AppConfigs.DEVICE_TYPE_NAME);
        if (error.hasErrors() || ObjectUtils.isEmpty(requestType)){
            throw new ValidationException(error);
        }
        accountAndService.changePassword(changePasswordParam, request, response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> resp = new Success<>();
        resp.setMessageCode(Message.PASSWORD_CHANGE_SUCCESSFUL);
        return respBuilder.body(resp);
    }

    @PutMapping(value = "/changeInfo")
    public ResponseEntity<Success<?>> changeInfo(@RequestBody ChangeInfoParam changeInfoParam, HttpServletRequest request, HttpServletResponse response){
        String requestType = request.getHeader(AppConfigs.DEVICE_TYPE_NAME);
        Object data =  accountAndService.changeInfo(changeInfoParam, request , response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> resp = new Success<>();
        resp.setMessageCode(Message.CHANGE_INFORMATION_SUCCESSFUL);
        resp.setData(data);
        return respBuilder.body(resp);
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<Success<?>> resetPassword(@RequestBody ResetPasswordParam resetPasswordParam,
                                                    BindingResult errors,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response){
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        accountAndService.resetPassword(resetPasswordParam, request ,response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> resp = new Success<>();
        resp.setMessageCode(Message.PASSWORD_RESET_SUCCESSFUL);
        return respBuilder.body(resp);
    }


    @PostMapping(value = "/super-admin/reset-password")
    public ResponseEntity<Success<?>> superAdminChangePassword(@RequestBody SuperAdminResetPasswordParam param,
                                                    BindingResult errors,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response){

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        accountAndService.superAdminChangePassword(param, request);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> resp = new Success<>();
        resp.setMessageCode(PASSWORD_RESET_SUCCESSFUL);
        return respBuilder.body(resp);
    }

    @GetMapping(value = "/admin-list")
    public ResponseEntity<Success<?>> getAdminList(@RequestParam(name = "isSuperAdmin") boolean isSuperAdmin,HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(accountAndService.getAdminList(isSuperAdmin, request, response));
        success.setMessageCode(Message.DATA_GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @GetMapping(value = "/isSuperAdmin")
    public ResponseEntity<Success<?>> getIsSuperAdmin(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(accountAndService.getIsSuperAdmin(request));
        success.setMessageCode(Message.DATA_GET_SUCCESSFUL);
        return respBuilder.body(success);
    }
}
