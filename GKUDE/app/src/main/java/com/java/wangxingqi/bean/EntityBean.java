package com.java.wangxingqi.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;

import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//描述、属性、关系及关联试题
@Getter
@Setter
@NoArgsConstructor
public class EntityBean extends SugarRecord implements Serializable{
    private String uri;
    private String label;
    private String category;
    private String course;
    private boolean visited = false;
    private boolean favorite = false;
    // relation (关系)
    private String relationStore;
    @Ignore
    @SerializedName("content")
    public List<RelationBean> relations;

    // property (属性)
    private String propertyStore;
    @Ignore
    @SerializedName("property")
    public List<PropertyBean> properties;

    // problem (关联试题)
    private String problemStore;
    @Ignore
    public List<ProblemBean> problems;

    public List<RelationBean> getRelationsFromStore() {
        Type type = new TypeToken<List<RelationBean>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(relationStore, type);
    }

    public List<PropertyBean> getPropertiesFromStore() {
        Type type = new TypeToken<List<PropertyBean>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(propertyStore, type);
    }

    public List<ProblemBean> getProblemsFromStore() {
        Type type = new TypeToken<List<ProblemBean>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(problemStore, type);
    }

    public String getDescription() {
        if (this.getCategory() == null) {
            return "暂无描述信息";
        }
        return this.getCategory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        EntityBean edukgEntity = (EntityBean) o;
        return Objects.equals(uri, edukgEntity.uri);
    }
}

