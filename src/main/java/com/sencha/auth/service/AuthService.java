package com.sencha.auth.service;

import com.sencha.auth.payload.request.LoginRequest;
import com.sencha.auth.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(LoginRequest loginRequest);
    ResponseEntity<?> signup(SignupRequest signupRequest);
}
