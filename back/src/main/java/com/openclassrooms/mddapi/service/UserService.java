package com.openclassrooms.mddapi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Themes;
import com.openclassrooms.mddapi.repository.ThemesRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ThemesRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    public class NotFoundException extends RuntimeException implements Serializable {
        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }

    // service to get all users
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    // service to get user by id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // service to save user
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatedAt(java.time.LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    // service to delete user
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            userRepository.delete(existingUser);
        } else {
            throw new NotFoundException("Enregistrement introuvable");
        }
    }

    // service to update password
    public User updatePassword(User updatePassword) {
        User existingUser = userRepository.findById(updatePassword.getId()).orElse(null);
        if (existingUser != null) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(updatePassword.getPassword()));
            existingUser.setUpdatedAt(java.time.LocalDateTime.now());
        }

        User updatedRecord = userRepository.save(existingUser);
        return updatedRecord;
    }

    // service to update user
    public User updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId()).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            User updatedRecord = userRepository.save(existingUser);
            return updatedRecord;
        } else {
            throw new NotFoundException("Enregistrement introuvable");
        }
    }

    // service to authenticate user
    public String authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                long expirationTimeInMillis = jwtConfig.getJwtExpirationMs();

                String token = Jwts.builder()
                        .setSubject(email)
                        .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                        .signWith(SignatureAlgorithm.HS256, jwtConfig.getJwtSecret())
                        .compact();
                return token;
            } else {
                throw new NotFoundException("Mot de passe incorrect");
            }
        } else {
            throw new NotFoundException("Utilisateur introuvable");
        }
    }

    // service to get email from token
    public String getEmailFromToken(String token) {

        String email = Jwts.parser()
                .setSigningKey(jwtConfig.getJwtSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return email;
    }

    // service to get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // service to get user by token
    public User getUserByToken(String token) {
        String email = getEmailFromToken(token);
        return getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));
    }

    // suscribe user to theme
    public User suscribeToTheme(Long userId, Long themeId) {
        User existingUser = userRepository.findById(userId).orElse(null);
        Themes existingTheme = themeRepository.findById(themeId).orElse(null);
        if (existingUser != null && existingTheme != null) {
            existingUser.setTheme(existingTheme);
        }
        User updatedRecord = userRepository.save(existingUser);
        return updatedRecord;
    }
    // unsubscribe user from theme
    public User unsuscribeToTheme(Long userId, Long themeId) {
        User existingUser = userRepository.findById(userId).orElse(null);
        Themes existingTheme = themeRepository.findById(themeId).orElse(null);
        if (existingUser != null && existingTheme != null) {
            existingUser.setTheme(null);
        }
        User updatedRecord = userRepository.save(existingUser);
        return updatedRecord;
    }
    // list of all themes for user
    public Themes getThemeByUser(Long userId) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            return existingUser.getTheme();
        } else {
            throw new NotFoundException("Enregistrement introuvable");
        }
    }
}
