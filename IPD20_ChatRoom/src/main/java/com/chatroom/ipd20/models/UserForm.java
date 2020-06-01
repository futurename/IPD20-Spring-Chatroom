package com.chatroom.ipd20.models;

import com.chatroom.ipd20.errors.validator.Email;
import com.chatroom.ipd20.errors.validator.PasswordConfirm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@PasswordConfirm
public class UserForm {
    @Size(min = 5, max = 50)
    private String name;

    @Email
    private String email;

    @NotNull
    @Size(min = 5, max = 50)
    private String pass1;

    @NotNull
    @Size(min = 5, max = 50)
    private String pass2;
}
