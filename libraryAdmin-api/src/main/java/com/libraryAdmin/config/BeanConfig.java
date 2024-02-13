package com.libraryAdmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class BeanConfig {

    @Bean("AUTH_EXCLUDED_URL_PATTERNS")
    public static List<String> excludedPaths() {
        return Arrays.asList(
                "/v1/signup",
                "/v1/signIn",
                "/v1/send-email-code",
                "/v1/reset-password",
                // delete
                "/v1/upload-image",
                "/v1/download/{newImageName}",
                "/v1/updates-image/{newImageName}",
                "v1/upload-file",
                "/v1/updates-file/{newFilename}",
                "/v1/all/category",
                "/v1/book/{id}",
                "/v1/isDeleted/{id}",
                "/v1/restore/{id}",
                "/v1/all/books",
                "/v1/delete/category/{id}",
                "/v1/attachment/{newFilename}",
                "/v1/all/12/books",
                "/v1/search/books"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("urlPathMatcher")
    public AntPathMatcher pathMatcher() { return new AntPathMatcher(); }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        source.setDefaultLocale(Locale.US);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000"
        ));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean("allowedImageType")
    public List<String> allowedImageType() {
        return Stream.of("image/jpeg",
                "image/png",
                "image/jpg"
        ).collect(Collectors.toList());
    }

    @Bean("allowedFileType")
    public List<String> allowedFileType() {
        return Stream.of( "application/pdf"
        ).collect(Collectors.toList());
    }
}
