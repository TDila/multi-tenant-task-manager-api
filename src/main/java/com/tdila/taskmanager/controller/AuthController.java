package com.tdila.taskmanager.controller;

import com.tdila.taskmanager.dto.LoginRequestDTO;
import com.tdila.taskmanager.dto.LoginResponseDTO;
import com.tdila.taskmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
            ){
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
