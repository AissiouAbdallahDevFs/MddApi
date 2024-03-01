package com.openclassrooms.mddapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import com.openclassrooms.mddapi.dto.ArticleMessagesDTO;
import com.openclassrooms.mddapi.dto.ArticleRequestDto;
import com.openclassrooms.mddapi.dto.ArticleWithMessagesDTO;
import com.openclassrooms.mddapi.dto.MessageDTO;
import com.openclassrooms.mddapi.dto.PostMessagesDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Themes;
import com.openclassrooms.mddapi.model.Messages;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.MessagesService;
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
    private MessagesService messagesService;

    @Autowired
    private ThemesService themesService;

    // get all articles
    @GetMapping("/articles")
    @ApiOperation(value = "Get All Articles", notes = "Returns all Articles.")
    public ResponseEntity<Iterable<ArticleMessagesDTO>> getArticles() {
        Iterable<ArticleMessagesDTO> articles = articleService.getArticles();

        if (articles != null) {
            
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // get article by id
    @GetMapping("articles/{id}")
    @ApiOperation(value = "Get Article by ID", notes = "Returns a Article by its ID.")
    public ResponseEntity<ArticleWithMessagesDTO> getArticleById(@PathVariable Long id) {
        Optional<Article> articleOptional = articleService.getArticleById(id);

        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            ArticleWithMessagesDTO articleWithMessagesDTO = new ArticleWithMessagesDTO();
            articleWithMessagesDTO.setId(article.getId());
            articleWithMessagesDTO.setTitle(article.getTitle());
            articleWithMessagesDTO.setDescription(article.getDescription());

            List<MessageDTO> messageDTOs = article.getMessages().stream()
                    .map(message -> {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setId(message.getId());
                        messageDTO.setUserUsername(message.getUser().getUsername());
                        messageDTO.setMessage(message.getMessage());
                        return messageDTO;
                    })
                    .collect(Collectors.toList());

            articleWithMessagesDTO.setMessages(messageDTOs);

            return new ResponseEntity<>(articleWithMessagesDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // get message by article_id
    @GetMapping("/articles/{articleId}/messages")
    public ResponseEntity<ArticleWithMessagesDTO> getMessageByArticleId(@PathVariable Long articleId) {
        Optional<Article> articleOptional = articleService.getArticleById(articleId);

        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            ArticleWithMessagesDTO articleWithMessagesDTO = new ArticleWithMessagesDTO();
            articleWithMessagesDTO.setId(article.getId());
            articleWithMessagesDTO.setTitle(article.getTitle());
            articleWithMessagesDTO.setDescription(article.getDescription());
            List<MessageDTO> messageDTOs = article.getMessages().stream()
            .map(message -> {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setId(message.getId());
                messageDTO.setUserUsername(message.getUser().getUsername());
                messageDTO.setMessage(message.getMessage());
                return messageDTO;
            })
            .collect(Collectors.toList());

            articleWithMessagesDTO.setMessages(messageDTOs);

            return new ResponseEntity<>(articleWithMessagesDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        }
    

    // post message by article_id
    @PostMapping("/articles/{articleId}/messages")
    public ResponseEntity<PostMessagesDto> saveMessages(@RequestHeader("Authorization") String authorizationHeader,
                                                @PathVariable Long articleId, 
                                                @RequestParam String message) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = userService.getUserByToken(token);
        Article article = articleService.getArticleById(articleId).get();
        System.err.println("article: " + article);
        Messages newMessage = new Messages();
        newMessage.setUser(user);
        newMessage.setMessage(message);
        article.getMessages();
        article.getMessages().add(newMessage);
        articleService.saveArticles(article);
        PostMessagesDto postMessagesDto = new PostMessagesDto();
        postMessagesDto.setMessage(message);
        postMessagesDto.setArticle_id(article.getId());
        postMessagesDto.setUser_id(user.getId());
        
                                                
        if (newMessage != null) {
            return new ResponseEntity<>(postMessagesDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
                                                    
    }

    // save article
    @PostMapping(value = "/articles")
    @ApiOperation(value = "Create a new Article", notes = "Creates a new Article.")
    public Article saveRentals(@RequestHeader("Authorization") String authorizationHeader,
                                @RequestBody ArticleRequestDto articleDto) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = userService.getUserByToken(token);
        Long themeId = articleDto.getTheme();
        System.err.println("theme: " + themeId);
        Themes themeIdObject = themesService.getThemesById(themeId);                       
        Article article = new Article() ;
        article.setTitle(articleDto.getTitle());
        article.setDescription(articleDto.getDescription());
        article.setAuthor(user);
        article.setTheme(themeIdObject);
        System.err.println("article: " + article);
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

