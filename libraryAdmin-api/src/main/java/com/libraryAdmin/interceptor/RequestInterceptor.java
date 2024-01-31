package com.libraryAdmin.interceptor;

import com.libraryAdmin.consts.AppConfigs;
import com.libraryAdmin.entity.Admin;
import com.libraryAdmin.exception.UnauthorisedException;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.repository.AdminRepository;
import com.libraryAdmin.utils.Crypto;
import com.libraryAdmin.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    static List<String> EXCLUDE_FROM_PANHANDLE = List.of( "/v1/signup",
            "/v1/signIn");

    @Autowired
    Crypto crypto;

    @Autowired
    CurrentSession currentSession;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader(AppConfigs.ACCESS_TOKEN);
        if (!EXCLUDE_FROM_PANHANDLE.contains(request.getServletPath()) && !StringUtils.isEmpty(accessToken)) {
            String decryptToken = crypto.decrypt(accessToken);
            if (ObjectUtils.isEmpty(decryptToken)) {
                throw new UnauthorisedException();
            }
            String email = jwtUtil.extractUserName(decryptToken);
            Admin user = adminRepository.findByEmail(email);
            currentSession.setEmail(email);
            currentSession.setId(user.getId());
            currentSession.setUserName(user.getUserName());
            currentSession.setFirstName(user.getFirstName());
            currentSession.setLastName(user.getLastName());
            currentSession.setContact(user.getContact());
        }
        currentSession.setLocale(request.getLocale());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
