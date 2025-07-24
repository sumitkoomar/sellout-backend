package com.sellout.userservice.service;

import com.sellout.userservice.dto.*;

public interface UserService {
    UserResponseDTO register(RegisterRequestDTO request);
    String login(LoginRequestDTO request);
    UserResponseDTO getUserByEmail(String email);
}
