package com.libraryAdmin.Controllers;


import com.libraryAdmin.consts.Message;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.model.params.SendEmailCodeParam;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.VerificationCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.libraryAdmin.consts.Message.SEND_SUCCESSFUL;


@RestController
@RequestMapping("/v1")
public class VerificationCodeController {

    @Autowired
    VerificationCodeService verificationCodeService;

    @PostMapping(value = "/send-email-code")
    public ResponseEntity<Success<?>> sendEmailCode(@RequestBody SendEmailCodeParam param,
                                                    BindingResult errors,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response){
        if (errors.hasErrors()) {
            throw new ValidationException();
        }
        verificationCodeService.sendCode(param, request);
        Success<?> s = new Success<>();
        s.setMessage(SEND_SUCCESSFUL);
        s.setMessageCode(SEND_SUCCESSFUL);
        return ResponseEntity.status(HttpStatus.OK).body(s);
    }
}