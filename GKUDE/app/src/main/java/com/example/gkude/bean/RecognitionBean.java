package com.example.gkude.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecognitionBean {
    private String entity_type;
    private String entity_url;
    private int start_index;
    private int end_index;
    private String entity;
}
