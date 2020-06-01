package com.chatroom.ipd20.services;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.sql.Blob;

@Service
public class BlobService {
    private EntityManager entityManager;

    @Autowired
    public BlobService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Blob createBlob(InputStream content, long size) {
        return Hibernate
                .getLobCreator(entityManager.unwrap(Session.class))
                .createBlob(content, size);
    }
}
