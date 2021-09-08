package com.java.wangxingqi.server;

import com.java.wangxingqi.bean.EntityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;

    private final UserDataSource dataSource;

    private final String prefix = "http://172.20.10.8:8080/";

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    public static UserRepository getInstance(UserDataSource dataSource) {
        if (instance == null) {
            instance = new UserRepository(dataSource);
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public Result<User> login(String username, String password) {
        // handle login
        Result<String> result = dataSource.login(username, password, prefix+"api/login");
        if (result.getStatus().equals(200)) {
            User user = new User();
            user.setUserToken(result.getData());
            user.setUsername(username);
            user.setFavorites(new ArrayList<>());
            user.setHistories(new ArrayList<>());
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

    public Result<String> updatePassword(String oldPassword, String newPassword) {
        return dataSource.updatePassword(user.getUserToken(), oldPassword, newPassword, prefix+"api/update/password");
    }

    public Result<List<EntityBean>> syncFavorites() {
        Result<List<EntityBean>> result = dataSource.getEntityList(user.getUserToken(), prefix+"api/favorite/get");
        if (result.getData()!=null) {
            // 先找本地缓存
            result.getData().replaceAll(entityBean -> {
                List<EntityBean> list = EntityBean.find(EntityBean.class, "uri = ?", entityBean.getUri());
                if (list.isEmpty()) {
                    return entityBean;
                } else {
                    return list.get(0);
                }
            });
            // 本地取并集
            user.getFavorites().removeAll(result.getData());
            user.getFavorites().addAll(result.getData());
            // 上传差集
            List<EntityBean> upload = new ArrayList<>(user.getFavorites());
            upload.removeAll(result.getData());
            for (EntityBean e : upload) {
                dataSource.changeEntityList(e, user.getUserToken(), prefix+"api/favorite/add");
            }
        }
        result.setData(user.getFavorites());
        return result;
    }

    public Result<String> addFavorite(EntityBean entityBean) {
        user.getFavorites().add(entityBean);
        return dataSource.changeEntityList(entityBean, user.getUserToken(), prefix+"api/favorite/add");
    }

    public Result<String> cancelFavorite(EntityBean entityBean) {
        user.getFavorites().remove(entityBean);
        return dataSource.changeEntityList(entityBean, user.getUserToken(), prefix+"api/favorite/cancel");
    }

    public Result<List<EntityBean>> syncHistories() {
        Result<List<EntityBean>> result = dataSource.getEntityList(user.getUserToken(), prefix+"api/history/get");
        if (result.getData()!=null) {
            // 先找本地缓存
            result.getData().replaceAll(entityBean -> {
                List<EntityBean> list = EntityBean.find(EntityBean.class, "uri = ?", entityBean.getUri());
                if (list.isEmpty()) {
                    return entityBean;
                } else {
                    return list.get(0);
                }
            });
            // 本地取并集
            user.getHistories().removeAll(result.getData());
            user.getHistories().addAll(result.getData());
            // 上传差集
            List<EntityBean> upload = new ArrayList<>(user.getHistories());
            upload.removeAll(result.getData());
            for (EntityBean e : upload) {
                dataSource.changeEntityList(e, user.getUserToken(), prefix+"api/history/add");
            }
        }
        result.setData(user.getHistories());
        return result;
    }

    public Result<String> addHistory(EntityBean entityBean) {
        user.getHistories().add(entityBean);
        return dataSource.changeEntityList(entityBean, user.getUserToken(), prefix+"api/history/add");
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