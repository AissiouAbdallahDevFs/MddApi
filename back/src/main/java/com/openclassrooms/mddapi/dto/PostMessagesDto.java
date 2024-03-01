package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class PostMessagesDto {
	
	private String message;
	private Long article_id;
	private Long user_id;
	
}
