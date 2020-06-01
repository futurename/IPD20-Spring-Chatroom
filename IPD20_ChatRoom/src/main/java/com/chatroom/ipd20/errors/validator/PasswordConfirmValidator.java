package com.chatroom.ipd20.errors.validator;

import com.chatroom.ipd20.models.UserForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, Object> {

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context){
        if(!(object instanceof UserForm)){
            throw new IllegalArgumentException("@PasswordConfirm only applies to UserForm");
        }

        UserForm userForm = (UserForm) object;
        return userForm.getPass1().equals(userForm.getPass2());
    }
}
