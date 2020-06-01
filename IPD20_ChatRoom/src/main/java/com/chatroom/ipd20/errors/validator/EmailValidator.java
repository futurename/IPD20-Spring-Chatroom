package com.chatroom.ipd20.errors.validator;

import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private Email constraintAnnotation;

    @Autowired
    UserRepository userRepo;

    @Override
    public void initialize(Email constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintContext){
        User user = userRepo.findByEmail(email).orElse(null);
        if(user != null) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext
                    .buildConstraintViolationWithTemplate( "Email already exist"  )
                    .addConstraintViolation();
            return false;
        }

        return email.matches("[a-zA-Z0-9_.-]+@(.+)[^.]");
    }
}
