package com.chatroom.ipd20.security.handler;

import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.BlobService;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private BlobService blobService;

    public CustomLoginSuccessHandler(BlobService blobService){
        this.blobService = blobService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        blobService.createTmpImageFileForUserIcon(userDetails);
        httpServletResponse.sendRedirect("/");
    }
}
