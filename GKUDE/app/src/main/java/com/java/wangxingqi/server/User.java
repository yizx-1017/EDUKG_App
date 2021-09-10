package com.java.wangxingqi.server;

import com.java.wangxingqi.bean.EntityBean;
import com.java.wangxingqi.bean.ProblemBean;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userToken;
    private String username;

    private List<EntityBean> favorites;
    private List<EntityBean> histories;
    private List<ProblemBean> wrongProblems;
    private Map<String, Integer> historyNum;
    private Map<String, Integer> wrongProblemNum;

}