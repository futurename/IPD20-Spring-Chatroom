package com.chatroom.ipd20.models;

import com.chatroom.ipd20.errors.validator.Email;
import com.chatroom.ipd20.errors.validator.Icon;
import com.chatroom.ipd20.errors.validator.PasswordConfirm;
import com.chatroom.ipd20.services.BlobService;
import lombok.Data;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.sql.Blob;

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

    @Icon
    private MultipartFile icon;

    private Blob iconBlob;
}
