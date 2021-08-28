package com.example.gkude.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelationBean implements Serializable {
    @SerializedName("subject_label")
    String subjectName;
    @SerializedName("object_label")
    String objectName;
    @SerializedName(value = "uri", alternate = {"object", "subject"})
    String entityUri;
    @SerializedName("predicate_label")
    String relationName;
    @SerializedName("predicate")
    String relationUri;
}
