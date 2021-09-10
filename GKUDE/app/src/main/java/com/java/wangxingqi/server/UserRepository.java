package com.java.wangxingqi.server;

import android.util.Log;

import com.java.wangxingqi.bean.EntityBean;
import com.java.wangxingqi.bean.ProblemBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;

    private final UserDataSource dataSource;

    private final String urlPrefix = "http://172.20.10.3:8080";

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
        Result<String> result = dataSource.login(username, password, urlPrefix + "/api/login");
        if (result.getStatus().equals(200)) {
            User user = new User();
            user.setUserToken(result.getData());
            user.setUsername(username);
            user.setFavorites(new LinkedList<>());
            user.setHistories(new LinkedList<>());
            user.setWrongProblems(new LinkedList<>());
            setLoggedInUser(user);
            syncFavorites();
            syncHistories();
            syncWrongProblems();
            syncHistoryNum();
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
        return dataSource.updatePassword(user.getUserToken(), oldPassword, newPassword, urlPrefix + "/api/update/password");
    }

    public Result<List<EntityBean>> syncFavorites() {
        Result<List<EntityBean>> result = dataSource.getEntityList(user.getUserToken(), urlPrefix + "/api/favorite/get");
        if (result.getData() != null) {
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
                dataSource.changeEntityList(e, user.getUserToken(), urlPrefix + "/api/favorite/add");
            }
        }
        result.setData(user.getFavorites());
        return result;
    }

    public Result<String> addFavorite(EntityBean entityBean) {
        user.getFavorites().add(entityBean);
        return dataSource.changeEntityList(entityBean, user.getUserToken(), urlPrefix + "/api/favorite/add");
    }

    public Result<String> cancelFavorite(EntityBean entityBean) {
        user.getFavorites().remove(entityBean);
        return dataSource.changeEntityList(entityBean, user.getUserToken(), urlPrefix + "/api/favorite/cancel");
    }

    public Result<List<EntityBean>> syncHistories() {
        Result<List<EntityBean>> result = dataSource.getEntityList(user.getUserToken(), urlPrefix + "/api/history/get");
        if (result.getData() != null) {
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
                dataSource.changeEntityList(e, user.getUserToken(), urlPrefix + "/api/history/add");
            }
        }
        syncHistoryNum();
        result.setData(user.getHistories());
        return result;
    }

    public Result<String> addHistory(EntityBean entityBean) {
        user.getHistories().add(entityBean);
        user.getHistoryNum().compute(entityBean.getCourse(), (k, v) -> v == null? 1 : v + 1);
        return dataSource.changeEntityList(entityBean, user.getUserToken(), urlPrefix + "/api/history/add");
    }

    public Result<String> addWrongProblem(ProblemBean problemBean) {
        user.getWrongProblems().add(problemBean);
        return dataSource.changeProblemList(problemBean, user.getUserToken(), urlPrefix + "/api/wrongProblem/add");
    }

    public Result<List<ProblemBean>> syncWrongProblems() {
        Result<List<ProblemBean>> result = dataSource.getProblemList(user.getUserToken(), urlPrefix + "/api/wrongProblem/get");
        if (result.getData() != null) {
            // 先找本地缓存
            result.getData().replaceAll(problemBean -> {
                List<ProblemBean> list = ProblemBean.find(ProblemBean.class, "q_id = ?", problemBean.getQID().toString());
                if (list.isEmpty()) {
                    return problemBean;
                } else {
                    return list.get(0);
                }
            });
            // 本地取并集
            user.getWrongProblems().removeAll(result.getData());
            user.getWrongProblems().addAll(result.getData());
            // 上传差集
            List<ProblemBean> upload = new ArrayList<>(user.getWrongProblems());
            upload.removeAll(result.getData());
            for (ProblemBean e : upload) {
                dataSource.changeProblemList(e, user.getUserToken(), urlPrefix + "/api/wrongProblem/add");
            }
        }
        result.setData(user.getWrongProblems());
        Log.i("User Repository", result.getData().toString());
        return result;
    }

    public List<ProblemBean> getProblemRecommendation(int num) {
        Map<ProblemBean, Integer> problemBeans = new HashMap<>();
        for (EntityBean entityBean : user.getFavorites()) {
            List<ProblemBean> list = entityBean.getProblemsFromStore();
            for (ProblemBean p : list) {
                problemBeans.compute(p, (k, v) -> v == null ? 2 : v + 2); // 收藏有更高的权重
            }
        }
        for (EntityBean entityBean : user.getHistories()) {
            List<ProblemBean> list = entityBean.getProblemsFromStore();
            for (ProblemBean p : list) {
                problemBeans.compute(p, (k, v) -> v == null ? 1 : v + 1);
            }
        }
        List<Map.Entry<ProblemBean, Integer>> entryList = new LinkedList<>(problemBeans.entrySet());
        entryList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        entryList = entryList.subList(0, num);
        List<ProblemBean> problemList = new ArrayList<>();
        for (Map.Entry<ProblemBean, Integer> m: entryList) {
            problemList.add(m.getKey());
        }
        return problemList;
    }

    public void syncHistoryNum() {
        Thread thread = new Thread(() ->{
            user.setHistoryNum(new HashMap<>());
            for (EntityBean e : user.getHistories()) {
                user.getHistoryNum().compute(e.getCourse(), (k, v) -> v == null ? 1: v + 1);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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