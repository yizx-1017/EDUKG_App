package com.example.gkude.bean;

import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemBean extends SugarRecord implements Serializable {
    private Integer qID;
    private String qBody;
    private String qAnswer;
}
