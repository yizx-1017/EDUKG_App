package com.example.gkude.utils;

import android.util.Log;
import android.util.Pair;


public class CategoryUtil {
    public static Pair<String, String> getSearchKeyword(String TAG) {
        switch (TAG) {
            case "语文":
                return new Pair("chinese", "中");
            case "英语":
                return new Pair("english", "语");
            case "数学":
                return new Pair("math", "数");
            case "物理":
                return new Pair("physics", "力");
            case "化学":
                return new Pair("chemistry", "反");
            case "生物":
                return new Pair("biology", "胞");
            case "历史":
                return new Pair("history", "史");
            case "地理":
                return new Pair("geo", "国");
            case "政治":
                return new Pair("politics", "法");
        }
        return new Pair("null", "null");
    }
}
