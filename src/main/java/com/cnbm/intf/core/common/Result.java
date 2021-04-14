package com.cnbm.intf.core.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 **/
@Slf4j
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    private Result<T> setCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        return this;
    }

    public String getMsg() {
        return msg;
    }

    private Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    private Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> Result<T> commonStatus(ResultCode resultCode, String msg, T data) {
        log.debug("success is code:{},msg:{},data:{}", resultCode.getMessage(), msg, JSONObject.toJSONString(data));
        return new Result<T>().setCode(resultCode).setMsg(msg).setData(data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return commonStatus(ResultCode.SUCCESS, msg, data);
    }

    public static <T> Result<T> success(String msg) {
        return commonStatus(ResultCode.SUCCESS, msg, null);
    }

    public static <T> Result<T> success(T data) {
        return commonStatus(ResultCode.SUCCESS, null, data);
    }

    public static <T> Result<T> failed(String msg, T data) {
        return commonStatus(ResultCode.FAILED, msg, data);
    }

    public static <T> Result<T> failed(String msg) {
        return commonStatus(ResultCode.FAILED, msg, null);
    }

    public static <T> Result<T> failed(T data) {
        return commonStatus(ResultCode.FAILED, null, data);
    }


}
