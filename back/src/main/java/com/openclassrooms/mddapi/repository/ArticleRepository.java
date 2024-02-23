package com.openclassrooms.mddapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.mddapi.model.Article;
import java.util.List;

import com.openclassrooms.mddapi.model.Themes;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByTheme(Themes theme);
	
}

