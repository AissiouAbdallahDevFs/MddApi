package com.openclassrooms.mddapi.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private User user;



    @Size(max = 5000)
    @NotNull
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    
    
}
