package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.services.HibernateSearchService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChannelController {

    @Autowired
    private HibernateSearchService searchService;

    @GetMapping("/channel")
    public String search(@RequestParam(required = false) String search, Model model)
    {
        List<Channel> searchResults = new ArrayList<>();

        if(search == null || search.isEmpty()){
            model.addAttribute("searchList", searchResults);
            return "channel";
        }

//        searchService.initializeHibernateSearch();

        searchResults = searchService.channelSearch(search);
        model.addAttribute("searchList", searchResults);
        return "channel";
    }
}
