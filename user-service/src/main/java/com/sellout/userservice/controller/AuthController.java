package com.sellout.userservice.controller;

import com.sellout.userservice.dto.*;
import com.sellout.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // You can later restrict this
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        UserResponseDTO registeredUser = userService.register(request);
        return ResponseEntity.ok(registeredUser);
    }

    // ✅ Login existing user
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = userService.login(request);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ✅ Get current user details (optional, useful for frontend)
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
