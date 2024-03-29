package com.libraryAdmin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key, Locale locale){
        return messageSource.getMessage(key , null ,locale);
    }

    public String getMessage(String key, Object[] args, Locale locale){
        return messageSource.getMessage(key, args ,locale);
    }
}
