package com.example.gkude.server;

import com.example.gkude.bean.EntityBean;

import java.util.List;

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

    private List<EntityBean> favorites;
    private List<EntityBean> histories;

}