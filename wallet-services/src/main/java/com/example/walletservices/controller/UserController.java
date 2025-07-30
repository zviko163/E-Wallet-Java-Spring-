package com.example.walletservices.controller;

import com.example.walletservices.config.CustomUserDetailsService;
import com.example.walletservices.dto.LoginRequest;
import com.example.walletservices.dto.RegistrationDto;
import com.example.walletservices.dto.UserResponseDto;
import com.example.walletservices.dto.UserUpdateDto;
import com.example.walletservices.model.AccountType;
import com.example.walletservices.model.User;
import com.example.walletservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.walletservices.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Qualifier("userServiceImpl")
    private final UserService USER_SERVICE;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Load user and generate token
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String token = jwtUtil.generateToken(String.valueOf(userDetails));

            // 3. Fetch full user data (optional)
            User user = USER_SERVICE.findByEmail(userDetails.getUsername());

            // 4. Return user + token in a structured response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);

            UserResponseDto userResponseDto = new UserResponseDto(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getAccounts()
            );
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationDto user) {
        return ResponseEntity.ok(USER_SERVICE.register(user));
    }

    @PutMapping("/addAccount/{id}")
    public ResponseEntity<User> addAccount(@PathVariable Integer id, @RequestParam AccountType accountType) {
        return ResponseEntity.ok(USER_SERVICE.addAccount(id, accountType));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(USER_SERVICE.findAll());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(USER_SERVICE.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDto user) {
        return ResponseEntity.ok(USER_SERVICE.updateUser(id, user));
    }

}
