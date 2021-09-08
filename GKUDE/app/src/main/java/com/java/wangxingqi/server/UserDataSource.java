package com.java.wangxingqi.server;

import com.java.wangxingqi.bean.EntityBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class UserDataSource {

    private final Gson gson = new Gson();
    private final OkHttpClient client = new OkHttpClient();

    public Result<String> login(String username, String password, String url) {
        System.out.println(username);
        System.out.println(password);
        String userToken = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJ1aWQiOjUsInN1YiI6IlVzZXIiLCJpc3MiOiJHS1VERV9TZXJ2ZXIiLCJleHAiOjE2MzAyOTQzNTAsImlhdCI6MTYzMDI5MTc1OCwianRpIjoiNWIwOTMwZDItZmYyNy00OTQxLThiYzMtYjVlZDIzMWIzZjU2In0." +
                "q3nhcW9QthZ6z45DoWevTsMUL9v7NzMPnlezreobq0w";
        return new Result<>(200, "OK!", userToken);
//        Result<String> result = new Result<>();
//        Thread thread = new Thread(() -> {
//            RequestBody formBody = new FormBody.Builder()
//                    .add("username", username).add("password", password).build();
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(formBody)
//                    .build();
//            System.out.println("I got here.... request");
//            try {
//                Response response = client.newCall(request).execute();
//                String json = Objects.requireNonNull(response.body()).string();
//                Type type = new TypeToken<Result<String>>() {
//                }.getType();
//                Result<String> private_result = gson.fromJson(json, type);
//                result.setData(private_result.getData());
//                result.setMsg(private_result.getData());
//                result.setStatus(private_result.getStatus());
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("The wrong message is>>>");
//                System.out.println(e.getMessage());
//            }
//        });
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (result.getStatus().equals(200))
//            return result;
//        else
//            return new Result<>(400, "bad request", "Error logging in");
    }

    public Result<String> updatePassword(String userToken, String oldPassword, String newPassword, String url) {
        Result<String> result = new Result<>();
        Thread thread = new Thread(()->{
            RequestBody formBody = new FormBody.Builder().add("oldPassword", oldPassword)
                    .add("newPassword", newPassword).build();
            Request request = new Request.Builder().url(url).addHeader("token", userToken).post(formBody).build();
            try {
                Response response = client.newCall(request).execute();
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<Result<String>>() {
                }.getType();
                Result<String> privateResult = gson.fromJson(json, type);
                result.setStatus(privateResult.getStatus());
                result.setMsg(privateResult.getMsg());
                result.setData(privateResult.getData());
            } catch (IOException e) {
                result.setStatus(400);
                e.printStackTrace();
                System.out.println("The wrong message is>>>");
                System.out.println(e.getMessage());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result.getStatus().equals(200))
            return result;
        else
            return new Result<>(400, "bad request", null);
    }

    public Result<List<EntityBean>> getEntityList(String userToken, String url) {
        Result<List<EntityBean>> result = new Result<>();
        Thread thread = new Thread(() -> {
            Request request = new Request.Builder().url(url).addHeader("token", userToken).get().build();
            try {
                Response response = client.newCall(request).execute();
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<Result<List<EntityBean>>>() {
                }.getType();
                Result<List<EntityBean>> privateResult = gson.fromJson(json, type);
                result.setStatus(privateResult.getStatus());
                result.setMsg(privateResult.getMsg());
                result.setData(privateResult.getData());
            } catch (IOException e) {
                result.setStatus(400);
                e.printStackTrace();
                System.out.println("The wrong message is>>>");
                System.out.println(e.getMessage());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result.getStatus().equals(200))
            return result;
        else
            return new Result<>(400, "bad request", null);
    }

    public Result<String> changeEntityList(EntityBean entityBean, String userToken, String url) {
        Result<String> result = new Result<>();
        Thread thread = new Thread(()->{
           RequestBody formBody = new FormBody.Builder().add("uri", entityBean.getUri())
                   .add("label", entityBean.getLabel())
                   .add("course", entityBean.getCourse())
                   .add("category", entityBean.getCategory()).build();
           Request request = new Request.Builder().url(url)
                   .addHeader("token", userToken).post(formBody).build();
           try {
               Response response = client.newCall(request).execute();
               String json = Objects.requireNonNull(response.body()).string();
               Type type = new TypeToken<Result<String>>(){}.getType();
               Result<String> privateResult = gson.fromJson(json, type);
               result.setStatus(privateResult.getStatus());
               result.setMsg(privateResult.getMsg());
               result.setData(privateResult.getData());
           } catch (IOException e) {
               result.setStatus(400);
               e.printStackTrace();
               System.out.println("The wrong message is>>>");
               System.out.println(e.getMessage());
           }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result.getStatus().equals(200)||result.getStatus().equals(202))
            return result;
        else
            return new Result<>(400, "bad request", null);
    }

}