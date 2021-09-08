package com.java.wangxingqi.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer status;
    private String msg;
    private T data;
}