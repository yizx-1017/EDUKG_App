package com.example.gkude.bean;

import java.io.Serializable;

public class RelationBean implements Serializable {
    String name;
    boolean forward;
    Integer id;
    RelationBean() {}
    public RelationBean(String name, boolean forward, Integer id) {
        this.name = name;
        this.forward = forward;
        this.id = id;
    }
}
