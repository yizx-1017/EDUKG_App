package com.example.gkude;


import androidx.annotation.NonNull;

import com.example.gkude.bean.ResultBean;
import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.ProblemBean;
import com.example.gkude.bean.RecognitionBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fetch {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private static String id = null;

    Fetch() {
        if (id == null) {
            id = fetchId();
        }
    }

    public String getId() {
        return id;
    }

    public String fetchId() {
        String url = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
        FormBody formBody = new FormBody.Builder()
                .add("password", "gkude2021").add("phone", "18801356535").build();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody).build();
        System.out.println("I got here.... request fetchID");
        try {
            Response response = client.newCall(request).execute();
            String json = Objects.requireNonNull(response.body()).string();
            Type type = new TypeToken<EdukgResponse<String>>() {
            }.getType();
            EdukgResponse<String> edukgResponse = gson.fromJson(json, type);
            System.out.println("id get!");
            return edukgResponse.getId();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EntityBean> fetchInstanceList(@NonNull String course, @NonNull String searchKey) {
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course=%s&searchKey=%s&id=%s", course, searchKey, id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<List<EntityBean>>>() {
                }.getType();
                EdukgResponse<List<EntityBean>> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course=%s&searchKey=%s&id=%s", course, searchKey, id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    json = Objects.requireNonNull(response.body()).string();
                    edukgResponse = gson.fromJson(json, type);
                }
                List<EntityBean> list = edukgResponse.getData();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCourse(course);
                }
                return list;
            }
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public EntityBean fetchInfoByInstanceName(@NonNull EntityBean entityBean) {
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course=%s&name=%s&id=%s", entityBean.getCourse(), entityBean.getLabel(), id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request fetchInfoByInstanceName");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<EntityBean>>() {
                }.getType();
                EdukgResponse<EntityBean> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course=%s&name=%s&id=%s", entityBean.getCourse(), entityBean.getLabel(), id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    json = Objects.requireNonNull(response.body()).string();
                    edukgResponse = gson.fromJson(json, type);
                }
                entityBean.setRelations(edukgResponse.getData().getRelations());
                entityBean.setProperties(edukgResponse.getData().getProperties());
                if (entityBean.getRelations() != null) {
                    entityBean.setRelationStore(edukgResponse.getData().getRelations().toString());
                }
                if (entityBean.getProperties() != null) {
                    entityBean.setPropertyStore(edukgResponse.getData().getProperties().toString());
                }
                System.out.println("in fetch: entityBean.getRelations():");
                System.out.println(entityBean.getRelations());
                System.out.println("prepare to return entity");
                entityBean.save();
                return entityBean;
            } else {
                System.out.println("ops! error");
                return null;
            }

        } catch (IOException e) {
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
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<List<ResultBean>>>() {
                }.getType();
                EdukgResponse<List<ResultBean>> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("inputQuestion", inputQuestion).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response = client.newCall(request).execute();
                    json = Objects.requireNonNull(response.body()).string();
                    edukgResponse = gson.fromJson(json, type);
                }
                return edukgResponse.getData();
            }
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<RecognitionBean> fetchLinkInstance(@NonNull String course, @NonNull String context) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
        FormBody formBody = new FormBody.Builder().add("course", course)
                .add("context", context).add("id", id).build();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody).build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<LinkInstanceResponse>>() {
                }.getType();
                EdukgResponse<LinkInstanceResponse> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("context", context).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response = client.newCall(request).execute();
                    json = Objects.requireNonNull(response.body()).string();
                    edukgResponse = gson.fromJson(json, type);
                }
                return edukgResponse.getData().getResults();
            }
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public EntityBean fetchQuestionListByUriName(@NonNull EntityBean entityBean) {
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName=%s&id=%s", entityBean.getUri(), id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<List<ProblemBean>>>() {
                }.getType();
                EdukgResponse<List<ProblemBean>> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName=%s&id=%s", entityBean.getUri(), id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    json = Objects.requireNonNull(response.body()).string();
                    edukgResponse = gson.fromJson(json, type);
                }
                entityBean.setProblems(edukgResponse.getData());
                entityBean.setProblemStore(edukgResponse.getData().toString());
                return entityBean;
            }
            return null;
        } catch (IOException e) {
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
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<List<ResultBean>>>() {
                }.getType();
                EdukgResponse<List<ResultBean>> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("subjectName", subjectName).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response = client.newCall(request).execute();
                    json = Objects.requireNonNull(response.body()).string();
                    edukgResponse = gson.fromJson(json, type);
                }
                return edukgResponse.getData();
            }
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The wrong message is>>>");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}

@Data
class EdukgResponse<T> {
    private String code;
    private String msg;
    private String id;
    private T data;
}

@Data
class LinkInstanceResponse {
    private List<RecognitionBean> results;
}