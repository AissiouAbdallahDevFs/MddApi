package com.openclassrooms.mddapi.dto;




import lombok.Data;

@Data
public class ArticleRequestDto {
    private String title;
    private String description;
    private Long theme;

    // Getters et setters
}

