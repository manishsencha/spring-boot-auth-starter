package com.sencha.auth.payload.response;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String email;
    private String token;
}
