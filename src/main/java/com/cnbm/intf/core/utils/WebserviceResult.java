package com.cnbm.intf.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 *
 * @create 2019031022:59
 */
@Slf4j
public class WebserviceResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T data;

    public T getData() {
        return data;
    }

    private WebserviceResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> WebserviceResult<T> resultData(T data) {
        return new WebserviceResult<T>().setData(data);
    }
}
