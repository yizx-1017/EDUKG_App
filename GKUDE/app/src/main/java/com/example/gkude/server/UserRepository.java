package com.example.gkude.server;

import com.example.gkude.server.model.User;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;

    private final UserDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private UserRepository(UserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static UserRepository getInstance(UserDataSource dataSource) {
        if (instance == null) {
            instance = new UserRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(User user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<User> login(String username, String password) {
        // handle login
        Result<String> result = dataSource.login(username, password);
        if (result.getStatus().equals(200)) {
            User user = new User();
            user.setUserToken(result.getData());
            user.setUsername(username);
            setLoggedInUser(user);
        }
        return new Result<>(result.getStatus(), result.getMsg(), user);
    }
}