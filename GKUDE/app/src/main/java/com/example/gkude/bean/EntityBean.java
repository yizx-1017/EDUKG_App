package com.example.gkude.bean;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import com.orm.dsl.Ignore;

import org.javatuples.Triplet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//描述、属性、关系及关联试题
public class EntityBean extends SugarRecord implements Serializable{
    private String url;
    private String label;
    private String category;
    private String description;
    private String course;
    private boolean visited;
    // relation (关系)
    private String relations;
    @Ignore
    private List<RelationBean> _relations;
    // 关系名称；True:Forward(当前entity作为主语),False:Backward(作为宾语)；另一个entity

    // property (属性)
    private String properties;
    @Ignore
    private Map<String, String> _properties;

    // problem (关联试题)
    private String problems;
    @Ignore
    private List<ProblemBean> _problems;

//    public EntityBean() {
//        url = label = category = description = "";
//        course = "chinese"; visited = false;
//        relations = properties = problems = "";
//        _relations = null; _problems = null; _properties = null;
//    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public List<RelationBean> getRelations() {
        // TODO: to be tested.
        System.out.println("I got here getRelations");
        _relations = new ArrayList<RelationBean>();
        Gson gson = new Gson();
        System.out.println("raw_relations constructed");
        _relations = gson.fromJson(this.relations, _relations.getClass());
        System.out.println("raw_relations tranformed");
//        for(RelationBean rel: _relations) {
//
//        }
        System.out.println("_relations constructed");
        return _relations;
    }
    public void setRelations(String relations) {
        this.relations = relations;
    }
    public List<ProblemBean> getProblems() {
        // TODO: to be tested.
        Gson gson = new Gson();
        _problems = new ArrayList<ProblemBean>();
        ArrayList<Integer> problem_ids = new ArrayList<Integer>();
        problem_ids = gson.fromJson(this.problems, problem_ids.getClass());
        for(Integer problem_id: problem_ids) {
            _problems.add(ProblemBean.findById(ProblemBean.class, problem_id));
        }
        return _problems;
    }
    public void setProblems(String problems) {
        this.problems = problems;
    }
    public Map<String, String> getProperties() {
        // TODO: to be tested.
        Gson gson = new Gson();
        _properties = new HashMap<String, String>();
        _properties = gson.fromJson(this.properties, _properties.getClass());
        return _properties;
    }
    public void setProperties(String properties) {
        this.properties = properties;
    }

}

