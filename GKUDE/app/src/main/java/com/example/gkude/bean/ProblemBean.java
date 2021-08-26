package com.example.gkude.bean;

import com.orm.SugarRecord;

import java.io.Serializable;

public class ProblemBean extends SugarRecord implements Serializable {
    private Integer qID;
    private String qBody;
    private String qAnswer;

//    public ProblemBean() {
//        qID = 0;
//        qBody = ""; qAnswer = "";
//        System.out.println("init problem bean!!!");
//    }
    public Integer getqID() {
        return qID;
    }
    public void setqID(Integer qID) {
        this.qID = qID;
    }
    public String getqBody() {
        return qBody;
    }
    public void setqBody(String qBody) {
        this.qBody = qBody;
    }
    public String getqAnswer() {
        return qAnswer;
    }
    public void setqAnswer(String qAnswer) {
        this.qAnswer = qAnswer;
    }
}
