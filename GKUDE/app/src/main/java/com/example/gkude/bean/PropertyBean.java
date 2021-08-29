package com.example.gkude.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyBean implements Serializable {
    private String predicate;
    private String predicateLabel;
    private String object;

    @NonNull
    @Override
    public String toString() {
        return '{' +
                "\"predicate\": \"" + predicate + '\"' +
                ", \"predicateLabel\": \"" + predicateLabel + '\"' +
                ", \"object\": \"" + object + '\"' +
                '}';
    }
}
