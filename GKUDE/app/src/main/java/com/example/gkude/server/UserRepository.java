package com.example.gkude.server;

import com.example.gkude.bean.EntityBean;
import com.example.gkude.server.model.User;

import java.util.List;

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

    public static UserRepository getInstance(UserDataSource dataSource) {
        if (instance == null) {
            instance = new UserRepository(dataSource);
        }
        return instance;
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

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    public Result<List<EntityBean>> getFavorites() {
        Result<List<EntityBean>> result = dataSource.getEntityList(user.getUserToken(), "http://10.0.2.2:8080/api/favorite/get");
        // favorite 有可能是null，用时先检查
        user.setFavorites(result.getData());
        return result;
    }

    public Result<String> addFavorite(EntityBean entityBean) {
        return dataSource.changeEntityList(entityBean, user.getUserToken(), "http://10.0.2.2:8080/api/favorite/add");
    }

    public Result<String> cancelFavorite(EntityBean entityBean) {
        return dataSource.changeEntityList(entityBean, user.getUserToken(), "http://10.0.2.2:8080/api/favorite/canel");
    }

    public Result<List<EntityBean>> getHistories() {
        Result<List<EntityBean>> result = dataSource.getEntityList(user.getUserToken(), "http://10.0.2.2:8080/api/history/get");
        // 有可能是null，用时先检查
        user.setHistories(result.getData());
        return result;
    }

    public Result<String> addHistory(EntityBean entityBean) {
        return dataSource.changeEntityList(entityBean, user.getUserToken(), "http://10.0.2.2:8080/api/history/add");
    }

    // private constructor : singleton access
    private UserRepository(UserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void setLoggedInUser(User user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}