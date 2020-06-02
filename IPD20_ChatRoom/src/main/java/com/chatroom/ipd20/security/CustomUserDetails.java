package com.chatroom.ipd20.security;

import com.chatroom.ipd20.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Lob;
import java.sql.Blob;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    @Getter
    private int id;
    @Getter
    private String name;
    private String email;
    private String password;

    @Getter
    private Blob icon;
    @Setter @Getter
    private String base64Icon;

    public CustomUserDetails(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.icon = user.getIcon();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
