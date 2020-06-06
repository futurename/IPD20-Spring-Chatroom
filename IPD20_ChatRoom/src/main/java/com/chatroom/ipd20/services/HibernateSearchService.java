package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Channel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    private final int CHANNEL_PER_PAGE = 6;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    public HibernateSearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void initializeHibernateSearch() {
        try {
            FullTextEntityManager fullTextEntityManager
                    = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

    public List<Channel> channelSearch(String keyword, int page, int totalPage) {
        javax.persistence.Query persistenceQuery
                = getPersistenceQueryForChannel(keyword);

        persistenceQuery.setFirstResult((page - 1)* CHANNEL_PER_PAGE);
        persistenceQuery.setMaxResults(CHANNEL_PER_PAGE);

        List<Channel> result = persistenceQuery.getResultList();

        return result;
    }

    public int getTotalChannelPage(String keyword){

        javax.persistence.Query persistenceQuery
                = getPersistenceQueryForChannel(keyword);

        List result = persistenceQuery.getResultList();

        int totalChannel = result.size();
        int totalPage = (int) Math.ceil((double)totalChannel / CHANNEL_PER_PAGE);
        return totalPage;
    }

    private javax.persistence.Query getPersistenceQueryForChannel(String keyword){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Channel.class).get();

        org.apache.lucene.search.Query query;
        if(keyword == null || keyword.isEmpty()){
            query = qb.all().createQuery();
        } else {
            query = qb
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(1)
                    .withPrefixLength(1)
                    .onFields("title", "owner.name")
                    .matching(keyword)
                    .createQuery();
        }

        return fullTextEntityManager.createFullTextQuery(query, Channel.class);

    }
}
