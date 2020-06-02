package com.chatroom.ipd20.security;

import com.chatroom.ipd20.security.handler.CustomLoginSuccessHandler;
import com.chatroom.ipd20.security.handler.CustomLogoutSuccessHandler;
import com.chatroom.ipd20.services.BlobService;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    com.chatroom.ipd20.security.CustomUserDetailsService userDetailsService;

    @Autowired
    BlobService blobService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
            .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .successHandler(new CustomLoginSuccessHandler(blobService))
                .failureUrl("/login?error=true")
                .and()
            .logout()
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .permitAll();

        // not to deny access h2-console
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
            "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
            "/resources/static/**", "/fontawesome/**", "/fonts/**", "/tmp/**", "/userIcons/**",
            "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/favicon.png"
        );
    }
}
