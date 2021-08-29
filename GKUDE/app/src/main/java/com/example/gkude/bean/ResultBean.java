package com.example.gkude.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultBean {
    private String all;
    private String fsanswer;
    private String subject;
    private String tamplateContent;
    private double fs;
    private String filterStr;
    private String subjectUri;
    private String predicate;
    private double score;
    private boolean answerflag;
    private String attention;
    private String fsscore;
    private String value;
}
