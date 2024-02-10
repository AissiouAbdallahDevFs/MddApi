package com.openclassrooms.mddapi.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;

import com.openclassrooms.mddapi.model.Article;
import  com.openclassrooms.mddapi.repository.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;



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


    public Iterable<Article> getArticle() {
        Iterable<Article> article = articleRepository.findAll();
        return article;
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }
    
   public Article saveArticles(Article articles) {
        articles.setCreatedAt(java.time.LocalDateTime.now());
        Article savedRentals = articleRepository.save(articles);
        return savedRentals;
    }

    private void uploadFileToFolder(MultipartFile file) {
        try {
            String folderPath = "file/";
            String originalFileName = file.getOriginalFilename();
            
            Path folder = Paths.get(folderPath);
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
    
            Path targetPath = folder.resolve(originalFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Article updateArticles(Article updatedRentals) {
       
        Article existingArticles= articleRepository.findById(updatedRentals.getId()).orElse(null);
        
        if (existingArticles != null) {
            existingArticles.setTitle(updatedRentals.getTitle());
            existingArticles.setDescription(updatedRentals.getDescription());
            existingArticles.setUpdatedAt(updatedRentals.getUpdatedAt());
            Article updatedRecord = articleRepository.save(existingArticles);
            return updatedRecord;
        } else {
            throw new NotFoundException("Enregistrement introuvable");
        }
    }
    

    public void deleteArticles(Long articleId) {
       
        Article existingArticles = articleRepository.findById(articleId).orElse(null);
        
        if (existingArticles != null) {
        
            articleRepository.delete(existingArticles);
        } else {
           
            throw new NotFoundException("Enregistrement introuvable");
        }
    }

    

}
