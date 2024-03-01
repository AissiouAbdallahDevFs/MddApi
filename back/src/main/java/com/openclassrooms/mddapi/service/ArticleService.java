package com.openclassrooms.mddapi.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Themes;
import  com.openclassrooms.mddapi.repository.*;

import java.util.List;



@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;



    
    
    public class NotFoundException extends RuntimeException implements Serializable {
        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }

    // get all article
    public Iterable<Article> getArticle() {
        Iterable<Article> article = articleRepository.findAll();
        return article;
    }
    // get article by id
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }
    
    // save article
    public Article saveArticles(Article articles) {
        articles.setCreatedAt(java.time.LocalDateTime.now());
        Article savedRentals = articleRepository.save(articles);
        return savedRentals;
    }


    // update article
    public Article updateArticles(Article updatedRentals) {
       
        Article existingArticles= articleRepository.findById(updatedRentals.getId()).orElse(null);
        
        if (existingArticles != null) {
            existingArticles.setTitle(updatedRentals.getTitle());
            existingArticles.setDescription(updatedRentals.getDescription());
            existingArticles.setUpdatedAt(updatedRentals.getUpdatedAt());
            existingArticles.setAuthor(updatedRentals.getAuthor());
            existingArticles.setTheme(updatedRentals.getTheme());
            Article updatedRecord = articleRepository.save(existingArticles);
            return updatedRecord;
        } else {
            throw new NotFoundException("Enregistrement introuvable");
        }
    }
    
    // delete article
    public void deleteArticles(Long articleId) {
       
        Article existingArticles = articleRepository.findById(articleId).orElse(null);
        
        if (existingArticles != null) {
        
            articleRepository.delete(existingArticles);
        } else {
           
            throw new NotFoundException("Enregistrement introuvable");
        }
    }

    // get article by theme
    public List<Article> getArticlesByTheme(Themes theme) {
        return articleRepository.findByTheme(theme);
    }
    

}
