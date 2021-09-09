package com.java.wangxingqi.bean;

import androidx.annotation.NonNull;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ProblemBean problemBean = (ProblemBean) o;
        return Objects.equals(qID, problemBean.qID);
    }
}
