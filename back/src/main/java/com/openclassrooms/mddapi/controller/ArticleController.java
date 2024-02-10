package com.openclassrooms.mddapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.HashMap;
import java.util.Optional;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Themes;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ThemesService;
import com.openclassrooms.mddapi.service.UserService;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Api(tags = "Articles", description = "Operations related to Articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private ThemesService themesService;

    // get all articles
    @GetMapping("/articles")
    @ApiOperation(value = "Get all articles", notes = "Returns a list of all Articles.")
    public Map<String, List<Article>> getArticle() {
        List<Article> articleList = (List<Article>) articleService.getArticle();
        Map<String, List<Article>> response = new HashMap<String, List<Article>>();
        response.put("articles", articleList);
        return response;
    }
    
    // get article by id
    @GetMapping("articles/{id}")
    @ApiOperation(value = "Get Article by ID", notes = "Returns a Article by its ID.")
    public ResponseEntity<Article> getRentalById(@PathVariable Long id) {
        Optional<Article> article = articleService.getArticleById(id);

        if (article.isPresent()) {
            return new ResponseEntity<>(article.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // save article
    @PostMapping(value = "/articles")
    @ApiOperation(value = "Create a new Article", notes = "Creates a new Article.")
    public Article saveRentals(@RequestHeader("Authorization") String authorizationHeader,
                               @RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("theme") String theme){
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = userService.getUserByToken(token);
        Long themeId = Long.parseLong(theme);
        System.err.println("theme: " + themeId);
        Themes themeIdObject = themesService.getThemesById(themeId);                       
        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setAuthor(user);
        article.setTheme(themeIdObject);
        Article savedRentals = articleService.saveArticles(article);
        if (savedRentals != null) {
            return new ResponseEntity<>(savedRentals, HttpStatus.OK).getBody();
        } else {
            return null;
        }
    }

    // find article by theme
    @GetMapping("/articles/theme/{themeId}")
    public List<Article> getArticlesByTheme(@PathVariable Long themeId) {
        Themes theme = themesService.getThemesById(themeId);
        return articleService.getArticlesByTheme(theme);
    }
}

