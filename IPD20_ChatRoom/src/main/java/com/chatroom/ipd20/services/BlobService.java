package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.security.CustomUserDetails;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@Service
public class BlobService {
    private EntityManager entityManager;

    private final int ICON_SIZE_FOR_DB = 100;


    @Autowired
    public BlobService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    UserRepository userRepo;

    public Blob createBlob(InputStream content) throws IOException{
        return Hibernate
                .getLobCreator(entityManager.unwrap(Session.class))
                .createBlob(scaleIconImage(content, ICON_SIZE_FOR_DB));
    }

    public String blobToBase64String (Blob img) {
        try {
            byte[] encoded = Base64.getEncoder().encode(img.getBinaryStream().readAllBytes());
            String imgDataAsBase64 = new String(encoded);
            return "data:image/png;base64," + imgDataAsBase64;
        } catch (SQLException|IOException ex){
            System.out.println("Internal Error. Cannot convert Blob to base64.");
        }
        return null;
    }


    private byte[] scaleIconImage(InputStream content, int size) throws IOException{
        BufferedImage bufImg = ImageIO.read(content);
        if(bufImg.getWidth() > size || bufImg.getHeight() > size){
            bufImg = progressiveScaling(bufImg, size);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufImg, "png", os);
        return os.toByteArray();
    }

    private static BufferedImage progressiveScaling(BufferedImage before, Integer longestSideLength) {
        if (before != null) {
            Integer w = before.getWidth();
            Integer h = before.getHeight();

            Double ratio = h > w ? longestSideLength.doubleValue() / h : longestSideLength.doubleValue() / w;

            //Multi Step Rescale operation
            //This technique is described in Chris Campbell’s blog The Perils of Image.getScaledInstance(). As Chris mentions, when downscaling to something less than factor 0.5, you get the best result by doing multiple downscaling with a minimum factor of 0.5 (in other words: each scaling operation should scale to maximum half the size).
            while (ratio < 0.5) {
                BufferedImage tmp = scale(before, 0.5);
                before = tmp;
                w = before.getWidth();
                h = before.getHeight();
                ratio = h > w ? longestSideLength.doubleValue() / h : longestSideLength.doubleValue() / w;
            }
            BufferedImage after = scale(before, ratio);
            return after;
        }
        return null;
    }

    private static BufferedImage scale(BufferedImage imageToScale, Double ratio) {
        Integer dWidth = ((Double) (imageToScale.getWidth() * ratio)).intValue();
        Integer dHeight = ((Double) (imageToScale.getHeight() * ratio)).intValue();
        BufferedImage scaledImage = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
        graphics2D.dispose();
        return scaledImage;
    }
}
