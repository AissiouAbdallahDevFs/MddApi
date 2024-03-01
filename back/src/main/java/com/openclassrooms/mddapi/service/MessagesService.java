package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Messages;
import com.openclassrooms.mddapi.model.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.MessagesRepository;

@Service
public class MessagesService {


    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private ArticleService articleService;



    public class NotFoundException extends RuntimeException implements Serializable {
        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }

    // get message by article_id
    public Optional<Messages> getMessageByArticleId(Long ArticleId) {
        Article article = articleService.getArticleById(ArticleId).orElse(null);    
        Set<Messages> message = article.getMessages();
        return message.stream().findFirst();
    }

    // save message
    public Messages saveMessage(Messages message) {
        message.setCreatedAt(LocalDateTime.now());
        Messages savedMessage = messagesRepository.save(message);
        return savedMessage;
    }
    
    
}
