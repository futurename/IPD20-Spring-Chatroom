package com.chatroom.ipd20.errors.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IconValidator implements ConstraintValidator<Icon, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile iconImg, ConstraintValidatorContext constraintContext){
        String contentType = iconImg.getContentType().toLowerCase();

        if(iconImg.isEmpty()){ return true; }

        if(contentType.equals("image/jpeg")
                || contentType.equals("image/jpg")
                || contentType.equals("image/png"))
        {
            return true;
        }
        return false;
    }
}
