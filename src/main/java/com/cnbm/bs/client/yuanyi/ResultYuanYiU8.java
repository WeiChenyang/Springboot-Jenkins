package com.cnbm.bs.client.yuanyi;

import com.alibaba.fastjson.JSONObject;
import com.cnbm.intf.core.common.ResultCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 **/
@Slf4j
public class ResultYuanYiU8<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int msgCode;
    private T msg;

    public int getMsgCode() {
        return msgCode;
    }

    private ResultYuanYiU8<T> setCode(ResultCode resultCode) {
        this.msgCode = resultCode.getCode();
        return this;
    }

    public T getMsg() {
        return msg;
    }

    private ResultYuanYiU8<T> setMsg(T msg) {
        this.msg = msg;
        return this;
    }

    public static <T> ResultYuanYiU8<T> commonStatus(ResultCode resultCode, T msg) {
        log.debug("success is code:{},msg:{},data:{}", resultCode.getMessage(),JSONObject.toJSONString(msg));
        return new ResultYuanYiU8<T>().setCode(resultCode).setMsg(msg);
    }

    public static <T> ResultYuanYiU8<T> success(T msg) {
        return commonStatus(ResultCode.SUCCESS, msg);
    }

    public static <T> ResultYuanYiU8<T> failed(T msg) {
        return commonStatus(ResultCode.FAILED, msg);
    }
}
