package com.chatroom.ipd20.models;

import com.chatroom.ipd20.errors.validator.Email;
import com.chatroom.ipd20.errors.validator.Icon;
import com.chatroom.ipd20.errors.validator.PasswordConfirm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Blob;

@Getter
@Setter
public class ChannelForm {

    @NotNull
    @Size(min=5, max=100)
    private String title;

    @NotNull
    @Size(min=5, max=200)
    private String description;

    @Icon
    private MultipartFile icon;

    private Blob iconBlob;
}
