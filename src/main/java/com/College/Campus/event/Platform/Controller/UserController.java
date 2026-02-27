package com.College.Campus.event.Platform.Controller;

import com.College.Campus.event.Platform.Entity.User;
import com.College.Campus.event.Platform.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user){
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("Email Already registerd");
        }
        if (user.getRole()==null){
            return ResponseEntity.badRequest().body("Role must be provided (Student/organizer/Admin)");
        }
        User savedUser = userRepository.save(user);

        savedUser.setPassword(null);

        return ResponseEntity.ok(savedUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(  @RequestBody User loginUser) {

        Optional<User> optionalUser = userRepository.findByEmail(loginUser.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        user.setPassword(null);

        return ResponseEntity.ok(user);
    }
}
