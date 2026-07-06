package com.project.ecommerce.user.service;

import com.project.ecommerce.notification.service.EmailService;
import com.project.ecommerce.user.Role;
import com.project.ecommerce.user.dto.request.LoginRequest;
import com.project.ecommerce.user.dto.request.RegisterRequest;
import com.project.ecommerce.user.dto.response.LoginResponse;
import com.project.ecommerce.user.entity.User;
import com.project.ecommerce.user.repository.UserRepository;
import com.project.ecommerce.user.security.CustomUserDetails;
import com.project.ecommerce.user.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Override
    public void register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("User already registered");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        emailService.sendWelcomeNewUserEmail(user);
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails.getUsername());
        emailService.sendWelcomeEmail(userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new EntityNotFoundException("User not found")));
        return new LoginResponse(token);
    }
}
