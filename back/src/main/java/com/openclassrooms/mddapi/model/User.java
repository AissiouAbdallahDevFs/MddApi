package com.openclassrooms.mddapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String email;

	@NotNull
	private String username;

	@NotNull
	private String password;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	
}