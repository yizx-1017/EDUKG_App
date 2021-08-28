package com.example.gkude.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecognitionBean {
    private String entity_type;
    private String entity_url;
    private int start_index;
    private int end_index;
    private String entity;
}
