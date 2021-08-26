package com.example.gkude.bean;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import com.orm.dsl.Ignore;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//描述、属性、关系及关联试题
public class EntityBean extends SugarRecord implements Serializable{
    private String url;
    private String label;
    private String course;
    private boolean visited;
    // relation (关系)
    private String relations;
    // property (属性)
    private String properties;
    @Ignore
    private Map<String, String> _properties;
    // problem (关联试题)
    private String problems;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public boolean getVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public Map<String, String> getProperties() {
        Gson gson = new Gson();
        _properties = new HashMap<String, String>();
        _properties = gson.fromJson(this.properties, _properties.getClass());
        return _properties;
    }
    public void setProperties(String properties) {
        this.properties = properties;
    }

}

