package com.tdila.taskmanager.service.impl;

import com.tdila.taskmanager.dto.LoginRequestDTO;
import com.tdila.taskmanager.dto.LoginResponseDTO;
import com.tdila.taskmanager.entity.User;
import com.tdila.taskmanager.repository.UserRepository;
import com.tdila.taskmanager.service.AuthService;
import com.tdila.taskmanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        String token = jwtUtil.generateToken(user);

        return LoginResponseDTO.builder()
                .accessToken(token)
                .build();
    }
}
