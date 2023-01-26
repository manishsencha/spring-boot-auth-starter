package com.sencha.auth.payload.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    private String username;
    private String email;
    private String message;
}
