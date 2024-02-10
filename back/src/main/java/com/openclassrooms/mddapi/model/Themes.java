package com.openclassrooms.mddapi.model;



import javax.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "Themes")
public class Themes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private String created_at;

    private String updated_at;
    

}
