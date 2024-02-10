package com.openclassrooms.mddapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
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

    @GetMapping("/Articles")
    @ApiOperation(value = "Get all articles", notes = "Returns a list of all Articles.")
    public Map<String, List<Article>> getArticle() {
        List<Article> articleList = (List<Article>) articleService.getArticle();
        Map<String, List<Article>> response = new HashMap<String, List<Article>>();
        response.put("articles", articleList);
        return response;
    }
    

    @GetMapping("Articles/{id}")
    @ApiOperation(value = "Get Article by ID", notes = "Returns a Article by its ID.")
    public ResponseEntity<Article> getRentalById(@PathVariable Long id) {
        Optional<Article> article = articleService.getArticleById(id);

        if (article.isPresent()) {
            return new ResponseEntity<>(article.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/Articles")
    @ApiOperation(value = "Create a new Article", notes = "Creates a new Article.")
    public Article saveRentals(@RequestHeader("Authorization") String authorizationHeader,
                               @RequestParam("title") String title,
                               @RequestParam("description") String description){
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = userService.getUserByToken(token);

        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setAuthor(user);
        Article savedRentals = articleService.saveArticles(article);
                                

        return savedRentals;
    }
}

