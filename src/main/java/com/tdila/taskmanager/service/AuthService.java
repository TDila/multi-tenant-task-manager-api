package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.LoginRequestDTO;
import com.tdila.taskmanager.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}
