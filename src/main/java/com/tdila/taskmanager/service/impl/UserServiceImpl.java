package com.tdila.taskmanager.service.impl;

import com.tdila.taskmanager.dto.UserRegisterRequestDTO;
import com.tdila.taskmanager.entity.Role;
import com.tdila.taskmanager.entity.User;
import com.tdila.taskmanager.repository.UserRepository;
import com.tdila.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegisterRequestDTO request) {
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new IllegalArgumentException("Password do not match");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.SYSTEM_USER)
                .build();

        userRepository.save(user);
    }
}
