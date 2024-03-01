package com.openclassrooms.mddapi.dto;
import com.openclassrooms.mddapi.model.Themes;
import java.util.List;
import lombok.Data;


@Data
public class UserProfileDTO {
    private String username;
    private String email;
    private List<Themes> themes;
  
}