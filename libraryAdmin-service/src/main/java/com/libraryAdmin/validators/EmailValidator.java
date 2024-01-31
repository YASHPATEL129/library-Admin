package com.libraryAdmin.validators;

import com.libraryAdmin.repository.AdminRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    private final AdminRepository adminRepository;

    public EmailValidator(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }
        return !adminRepository.existsByEmail(email);
    }
}