package com.openclassrooms.mddapi.model;



import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
@Entity
@Table(name = "Themes")
public class Themes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;
    
    @NotNull
    private String description;

  	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;


    
    
    


}
