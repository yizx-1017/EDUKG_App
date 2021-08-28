package com.example.gkude.data;

import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.ProblemBean;
import com.example.gkude.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.XMLFormatter;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public String post_login(String username,String password){
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
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public Result<LoggedInUser> login(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        LoggedInUser fakeUser = new LoggedInUser("0",username);
        return new Result.Success<>(fakeUser);
//        try {
//            // TODO: handle loggedInUser authentication
//            // TODO: send to backend the username and password, check if valid
//
//            final boolean[] result_from_backend = {false};
//            final String[] userID = new String[1];
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    String test_string = post_to_edukg();
////                    System.out.println(test_string);
//                    String result = post_login(username, password);
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        int status = jsonObject.getInt("status");
//                        if(status == 200) {
//                            result_from_backend[0] = true;
//                            userID[0] = jsonObject.getString("data");
//                        }
//                        System.out.println(status);
//                        System.out.println(userID[0]);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(result);
//                }
//            });
//            thread.start();
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            userID[0],
//                            username);
//            if(result_from_backend[0])
//                return new Result.Success<>(fakeUser);
//            else // TODO: change exception and description
//                return new Result.Error(new IOException("Error logging in"));
//        } catch (Exception e) {
//            return new Result.Error(new IOException("Error logging in", e));
//        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}