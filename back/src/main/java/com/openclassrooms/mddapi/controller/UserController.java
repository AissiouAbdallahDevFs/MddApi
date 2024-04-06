package com.openclassrooms.mddapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.openclassrooms.mddapi.dto.UserLoginRequest;
import com.openclassrooms.mddapi.dto.UserProfileDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Themes;
import com.openclassrooms.mddapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Api(tags = "Users", description = "Operations related to users")
public class UserController {

    @Autowired
    private UserService userService;

    // get all users
    @GetMapping("/users")
    @ApiOperation(value = "Get all users", notes = "Returns a list of all users.")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    // get user by id
    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get user by ID", notes = "Returns a user by its ID.")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // create a new user
    @PostMapping("auth/register")
    @ApiOperation(value = "Create a new user", notes = "Creates a new user.")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(user.getEmail());
        if (!matcher.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }
        User existingUser = userService.getUserByEmail(user.getEmail()).orElse(null);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        } else {
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        }
    }

    // authenticate user
    @PostMapping("auth/login")
    @ApiOperation(value = "Authenticate user", notes = "Authenticate a user and return a JWT token.")
    public ResponseEntity<String> authenticateUser(@RequestBody UserLoginRequest loginRequest) {
        System.err.println(loginRequest);
        String token = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (token != null) {
            HashMap<String, String> response = new HashMap<>();
            response.put("token", token);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(response);
                return ResponseEntity.ok(jsonResponse);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error converting response to JSON");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // get current user
    @GetMapping("/auth/me")
    @ApiOperation(value = "Get current user", notes = "Returns the current user.")
    public ResponseEntity<UserProfileDTO> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        User user = userService.getUserByToken(token);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setUsername(user.getUsername());
        List<Themes> themes = user.getThemes().stream()
                .toList();
        userProfileDTO.setThemes(themes);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
    }

    // update email and username
    @PutMapping("/auth/me")
    @ApiOperation(value = "Update email and username", notes = "Updates the email and username of the current user.")
    public ResponseEntity<UserProfileDTO> updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody User updatedUser) {
        String token = authorizationHeader.substring(7);
        String email = userService.getEmailFromToken(token);
        User user = userService.getUserByEmail(email).get();
        user.setEmail(updatedUser.getEmail());
        user.setUsername(updatedUser.getUsername());
        User updatedRecord = userService.updateUser(user);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setEmail(updatedRecord.getEmail());
        userProfileDTO.setUsername(updatedRecord.getUsername());
        List<Themes> themes = user.getThemes().stream()
                .toList();
        userProfileDTO.setThemes(themes);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
    }

    // suscribe to a theme
    @PostMapping("/auth/subscribe/{themeId}")
    @ApiOperation(value = "Subscribe to a theme", notes = "Subscribe to a theme.")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader , @PathVariable Long themeId) {
        String token = authorizationHeader.substring(7);
        User user = userService.getUserByToken(token);
        User updatedUser = userService.subscribeToTheme(user.getId(), themeId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        
    }
    // unsubscribe from a theme
    @DeleteMapping("/auth/unsubscribe/{themeId}")
    @ApiOperation(value = "Unsubscribe from a theme", notes = "Unsubscribe from a theme.")
    public ResponseEntity<User> unsubscribeFromTheme(@RequestHeader("Authorization") String authorizationHeader , @PathVariable Long themeId) {
        String token = authorizationHeader.substring(7);
        User user = userService.getUserByToken(token);
        System.err.println("user: " + user.getId() + " theme: " + themeId);
        userService.unsubscribeFromTheme(user.getId(), themeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //list of user themes
    @GetMapping("/auth/themes")
    @ApiOperation(value = "Get user themes", notes = "Returns the themes of the current user.")
    public ResponseEntity<Set<Themes>> getUserThemes(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        User user = userService.getUserByToken(token);
        return new ResponseEntity<>(user.getThemes(), HttpStatus.OK);
    }
}
