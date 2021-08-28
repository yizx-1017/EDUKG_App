package com.example.gkude.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import com.orm.dsl.Ignore;

import java.io.Serializable;
import java.util.List;

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
    private String description;
    private String course;
    private boolean visited = false;
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
}

