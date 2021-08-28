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
    private String id;

    Fetch() {
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
            String json = Objects.requireNonNull(response.body()).string();
            Type type = new TypeToken<EdukgResponse<String>>(){}.getType();
            EdukgResponse<String> edukgResponse = gson.fromJson(json, type);
            return edukgResponse.getId();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EntityBean> fetchInstanceList(@NonNull String course,@NonNull String searchKey) {
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course=%s&searchKey=%s&id=%s",course,searchKey,id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<List<EntityBean>>>(){}.getType();
                EdukgResponse<List<EntityBean>> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    this.id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course=%s&searchKey=%s&id=%s",course,searchKey,id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    json = Objects.requireNonNull(response.body()).string();
                    edukgResponse = gson.fromJson(json, type);
                }
                List<EntityBean> list = edukgResponse.getData();
                for (int i=0; i<list.size(); i++) {
                    list.get(i).setCourse(course);
                }
                return list;
            }
            return null;
        } catch (IOException e) {
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
                String json = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<EdukgResponse<EntityBean>>(){}.getType();
                EdukgResponse<EntityBean> edukgResponse = gson.fromJson(json, type);
                if (edukgResponse.getCode().equals("-1")) {
                    this.id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course=%s&name=%s&id=%s",entityBean.getCourse(),entityBean.getLabel(),id);
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
                return entityBean;
            } else {
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
                List<ResultBean> result = new ArrayList<>();
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).string());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("inputQuestion", inputQuestion).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).string());
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
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).string());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("context", context).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response =  client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).string());
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
        String url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName=%s&id=%s", entityBean.getUri(), id);
        Request request = new Request.Builder().url(url).get().build();
        System.out.println("I got here.... request");
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                List<ProblemBean> result = new ArrayList<>();
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).string());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    url = String.format("http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName=%s&id=%s", entityBean.getUri(), id);
                    request = new Request.Builder().url(url).get().build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).string());
                }
                // 获取列表，暂时删去
//                JSONArray array = root.getJSONArray("data");
//                for (int i=0; i<root.length(); i++) {
//                    ProblemBean problemBean = gson.fromJson(array.getJSONObject(i).toString(), ProblemBean.class);
//                    result.add(problemBean);
//                }
                entityBean.setProblemStore(root.getString("data"));
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
                JSONObject root = new JSONObject(Objects.requireNonNull(response.body()).string());
                if (root.getString("code").equals("-1")) {
                    this.id = fetchId();
                    formBody = new FormBody.Builder().add("course", course)
                            .add("subjectName", subjectName).add("id", id).build();
                    request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(formBody).build();
                    response = client.newCall(request).execute();
                    root = new JSONObject(Objects.requireNonNull(response.body()).string());
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

@Data
class EdukgResponse<T> {
    private String code;
    private String msg;
    private String id;
    private T data;
}