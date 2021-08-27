package com.example.gkude;


import androidx.annotation.NonNull;

import com.example.gkude.bean.ResultBean;
import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.ProblemBean;
import com.example.gkude.bean.RecognitionBean;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fetch {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private String id;

    public Fetch() {
        this.id = fetchId();
    }

    public String getId() {
        return this.id;
    }

    public String fetchId() {
        String url = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
        FormBody formBody = new FormBody.Builder().add("phone", "18801356535")
                .add("password", "gkude2021").build();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody).build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(Objects.requireNonNull(response.body()).toString());
            return responseBody.getString("id");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EntityBean> fetchInstanceList(@NonNull String searchKey,@NonNull String course) {
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course=%s&searchKey=%s&id=%s",course,searchKey,id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                List<EntityBean> result = new ArrayList<>();
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course=%s&searchKey=%s&id=%s",course,searchKey,id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                }
                JSONArray array = root.getJSONArray("data");
                for (int i=0; i<array.length(); i++) {
                    EntityBean entityBean = gson.fromJson(array.getJSONObject(i).toString(), EntityBean.class);
                    result.add(entityBean);
                }
                return result;
            }
            return null;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public EntityBean fetchInfoByInstanceName(@NonNull EntityBean entityBean) {
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course=%s&name=%s&id=%s",entityBean.getCourse(),entityBean.getLabel(),id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course=%s&name=%s&id=%s",entityBean.getCourse(),entityBean.getLabel(),id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                }
                System.out.println(root.getString("property"));
                entityBean.setProperties(root.getString("property"));
                entityBean.setRelations(root.getString("content"));
                entityBean.save();
                return entityBean;
            } else {
                return null;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ResultBean> fetchInputQuestion(@NonNull String course, @NonNull String inputQuestion) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
        FormBody formBody = new FormBody.Builder().add("course", course)
                .add("inputQuestion", inputQuestion).add("id", id).build();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody).build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                List<ResultBean> result = new ArrayList<>();
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("inputQuestion", inputQuestion).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                }
                JSONArray array = root.getJSONArray("data");
                for (int i=0; i<array.length(); i++){
                    ResultBean resultBean = gson.fromJson(array.getJSONObject(i).toString(), ResultBean.class);
                    result.add(resultBean);
                }
                return result;
            }
            return null;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<RecognitionBean> fetchLinkInstance(@NonNull String context, @NonNull String course) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
        FormBody formBody = new FormBody.Builder().add("course", course)
                .add("context", context).add("id", id).build();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody).build();
        System.out.println("I got here.... request");
        try {
            Response response =  client.newCall(request).execute();
            if (response.isSuccessful()) {
                List<RecognitionBean> result = new ArrayList<>();
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("context", context).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response =  client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                }
                JSONArray array = root.getJSONObject("data").getJSONArray("results");
                for (int i=0; i<array.length(); i++){
                    RecognitionBean recognitionBean = gson.fromJson(array.getJSONObject(i).toString(), RecognitionBean.class);
                    result.add(recognitionBean);
                }
                return result;
            }
            return null;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public EntityBean fetchQuestionListByUriName(@NonNull EntityBean entityBean) {
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName=%s&id=%s", entityBean.getUrl(), id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                List<ProblemBean> result = new ArrayList<>();
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName=%s&id=%s", entityBean.getUrl(), id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                }
                // 获取列表，暂时删去
//                JSONArray array = root.getJSONArray("data");
//                for (int i=0; i<root.length(); i++) {
//                    ProblemBean problemBean = gson.fromJson(array.getJSONObject(i).toString(), ProblemBean.class);
//                    result.add(problemBean);
//                }
                entityBean.setProblems(root.getString("data"));
                entityBean.save();
                return entityBean;
            }
            return null;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ResultBean> fetchRelatedSubject(@NonNull String course, @NonNull String subjectName) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject";
        FormBody formBody = new FormBody.Builder().add("course", course)
                .add("subjectName", subjectName).add("id", id).build();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody).build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                List<ResultBean> result = new ArrayList<>();
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("subjectName", subjectName).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).toString());
                }
                JSONArray array = root.getJSONArray("data");
                for (int i=0; i<array.length(); i++){
                    ResultBean resultBean = gson.fromJson(array.getJSONObject(i).toString(), ResultBean.class);
                    result.add(resultBean);
                }
                return result;
            }
            return null;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
