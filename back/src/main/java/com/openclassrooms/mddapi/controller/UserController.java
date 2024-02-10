package com.openclassrooms.mddapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import com.openclassrooms.mddapi.dto.UserLoginRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Api(tags = "Users", description = "Operations related to users")
public class UserController {

    @Autowired
    private UserService userService;
    

    @GetMapping("/users")
    @ApiOperation(value = "Get all users", notes = "Returns a list of all users.")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

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

    @PostMapping("auth/register")
    @ApiOperation(value = "Create a new user", notes = "Creates a new user.")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        if (savedUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    
    
   
    @PostMapping("auth/login")
    @ApiOperation(value = "Authenticate user", notes = "Authenticate a user and return a JWT token.")
    public ResponseEntity<String> authenticateUser(@RequestBody UserLoginRequest loginRequest) {
        String token = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/auth/me")
    @ApiOperation(value = "Get current user", notes = "Returns the current user.")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String email = userService.getEmailFromToken(token);
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

