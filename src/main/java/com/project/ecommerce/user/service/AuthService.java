package com.project.ecommerce.user.service;

import com.project.ecommerce.user.dto.request.LoginRequest;
import com.project.ecommerce.user.dto.request.RegisterRequest;
import com.project.ecommerce.user.dto.response.LoginResponse;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
