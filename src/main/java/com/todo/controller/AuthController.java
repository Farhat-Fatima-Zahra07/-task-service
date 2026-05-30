package com.todo.controller;

import com.todo.model.User;
import com.todo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        User user = authService.register(
                body.get("username"),
                body.get("email"),
                body.get("password")
        );
        return ResponseEntity.ok(Map.of(
                "message", "Inscription réussie",
                "userId", user.getId(),
                "username", user.getUsername()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        User user = authService.login(
                body.get("username"),
                body.get("password")
        );
        return ResponseEntity.ok(Map.of(
                "message", "Connexion réussie",
                "userId", user.getId(),
                "username", user.getUsername()
        ));
    }
}