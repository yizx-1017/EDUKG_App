package com.example.gkude.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userToken;
    private String username;

}