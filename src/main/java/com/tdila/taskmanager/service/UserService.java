package com.tdila.taskmanager.service;

import com.tdila.taskmanager.dto.UserRegisterRequestDTO;

public interface UserService {
    void registerUser(UserRegisterRequestDTO request);
}
