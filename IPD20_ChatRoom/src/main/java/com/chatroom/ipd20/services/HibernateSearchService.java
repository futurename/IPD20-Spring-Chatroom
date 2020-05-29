package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Channel;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class HibernateSearchService {

    private EntityManager entityManager;

    @Autowired
    public HibernateSearchService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    // User MassIndexer for performance(Don't need for now)
    public void initializeHibernateSearch() {
        try {
            FullTextEntityManager fullTextEntityManager
                    = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
            System.out.println("Create Indexer?");
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

//    @Transactional
    public List<Channel> channelSearch(String searchTerm) {
//        entityManager.getTransaction().begin();
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Channel.class).get();
        org.apache.lucene.search.Query query = qb
                .keyword()
                .onFields("title", "description", "owner.name")
                .matching(searchTerm)
                .createQuery();

        javax.persistence.Query persistenceQuery =
                fullTextEntityManager.createFullTextQuery(query, Channel.class);

        List result = persistenceQuery.getResultList();
//        entityManager.getTransaction().commit();
        return result;
    }
}
