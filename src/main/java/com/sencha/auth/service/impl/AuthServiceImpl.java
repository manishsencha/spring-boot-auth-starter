package com.sencha.auth.service.impl;

import com.sencha.auth.jwt.JWTUtil;
import com.sencha.auth.models.User;
import com.sencha.auth.payload.request.LoginRequest;
import com.sencha.auth.payload.request.SignupRequest;
import com.sencha.auth.payload.response.LoginResponse;
import com.sencha.auth.payload.response.MessageResponse;
import com.sencha.auth.payload.response.SignupResponse;
import com.sencha.auth.repository.UserRepository;
import com.sencha.auth.security.service.UserDetailsImpl;
import com.sencha.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    public  ResponseEntity<?> login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtil.generateTokenFromUsername(loginRequest.getUsername());
        return ResponseEntity.ok().body(
                new LoginResponse(userDetails.getUsername(), userDetails.getEmail(),token)
        );
    }

    public ResponseEntity<?> signup(SignupRequest signupRequest) {

        if(userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("User Already exists for this username"), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("User Already exists for this email"), HttpStatus.BAD_REQUEST);
        }

        try {
            User user = new User(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getEmail());
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(new SignupResponse( savedUser.getUsername(), savedUser.getEmail(), "User registration successful"), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new MessageResponse("Something went wrong at server side"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
