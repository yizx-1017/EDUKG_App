package com.example.gkude.server;

import com.example.gkude.server.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

    public String post_login(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add(
                "username", username).add(
                "password", password).build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/api/login")
                .post(formBody)
                .build();
        System.out.println("I got here.... request");
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public Result<String> login(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        String userToken = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJ1aWQiOjUsInN1YiI6IlVzZXIiLCJpc3MiOiJHS1VERV9TZXJ2ZXIiLCJleHAiOjE2MzAyOTQzNTAsImlhdCI6MTYzMDI5MTc1OCwianRpIjoiNWIwOTMwZDItZmYyNy00OTQxLThiYzMtYjVlZDIzMWIzZjU2In0." +
                "q3nhcW9QthZ6z45DoWevTsMUL9v7NzMPnlezreobq0w";
        // return new Result<>(200, "OK!", userToken);
        try {
            // handle loggedInUser authentication
            // send to backend the username and password, check if valid
            Result<String> result = new Result<>();
            Thread thread = new Thread(() -> {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("username", username).add("password", password).build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/login")
                        .post(formBody)
                        .build();
                System.out.println("I got here.... request");
                try {
                    Response response = client.newCall(request).execute();
                    String json = Objects.requireNonNull(response.body()).string();
                    Type type = new TypeToken<Result<String>>(){}.getType();
                    Result<String> private_result = gson.fromJson(json, type);
                    result.setData(private_result.getData());
                    result.setMsg(private_result.getData());
                    result.setStatus(private_result.getStatus());
                } catch (Exception e) {
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
                return new Result<>(400, "bad request", "Error logging in");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(400, "bad request", "Error logging in");
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}