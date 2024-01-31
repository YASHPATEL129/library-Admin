package com.libraryAdmin.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.libraryAdmin.consts.AppConfigs;
import com.libraryAdmin.hashRepository.UserSessionTrackerRepo;
import com.libraryAdmin.utils.Crypto;
import com.libraryAdmin.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.libraryAdmin.helper.SystemHelper.validateDevice;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    Crypto crypto;

    @Autowired
    UserSessionTrackerRepo userSessionTrackerRepo;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        AtomicReference<Boolean> doFilter = new AtomicReference<>(true);
        extractAuthToken(request).ifPresent(token -> {
            try {
                JWTVerifier verifier = JWT.require(jwtUtil.getJwtAlgo()).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                setSecurityContext(decodedJWT);
            } catch (Exception e) {
                doFilter.set(false);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                try {
                    response.getWriter().write("Unauthorized");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        if (doFilter.get())
            filterChain.doFilter(request, response);
    }

    private void setSecurityContext(DecodedJWT decodedJWT) {
        String userName = decodedJWT.getSubject();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    private Optional<String> extractAuthToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AppConfigs.ACCESS_TOKEN);
        if (!StringUtils.isEmpty(accessToken)) {
            String decryptedToken = crypto.decrypt(accessToken);
            if (!ObjectUtils.isEmpty(decryptedToken)){
                String userEmail = jwtUtil.extractUserName(decryptedToken);
                return (validateDevice(request , userEmail , userSessionTrackerRepo)) ? Optional.of(decryptedToken) : Optional.empty();
            }

        }
        return Optional.empty();
    }
}