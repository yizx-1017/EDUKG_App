package com.example.gkude.bean;

import androidx.annotation.NonNull;

import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProblemBean extends SugarRecord implements Serializable {
    private Integer qID;
    private String qBody;
    private String qAnswer;

    @NonNull
    @Override
    public String toString() {
        return '{' +
                "\"qID: \"" + qID +
                ", \"qBody\": \"" + qBody + '\"' +
                ", \"qAnswer\": \"" + qAnswer + '\"' +
                '}';
    }
}
