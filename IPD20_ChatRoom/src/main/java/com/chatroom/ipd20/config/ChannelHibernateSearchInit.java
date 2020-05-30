package com.chatroom.ipd20.config;

import com.chatroom.ipd20.services.HibernateSearchService;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

// When application starts, this executed.
@Component
public class ChannelHibernateSearchInit implements ApplicationListener<ContextRefreshedEvent> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event){

        HibernateSearchService searchService = new HibernateSearchService(entityManager);
        searchService.initializeHibernateSearch();
    }
}
