package com.cnbm.intf.core.webservice;

import com.alibaba.fastjson.JSON;
import com.cnbm.intf.core.model.CnbmIntf;
import com.cnbm.intf.core.model.CnbmIntfLog;
import com.cnbm.intf.core.utils.CnbmLogUtils;
import com.cnbm.intf.core.utils.CxfSoaContants;
import com.cnbm.intf.core.utils.ExtStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.SocketTimeoutException;

/**
 *
 * @create 2019031117:31
 */
@Slf4j
public abstract class CommonWebservice {

    protected CnbmIntf cnbmIntf;//接口管理对象
    protected CnbmIntfLog cnbmIntfLog = new CnbmIntfLog();//日志信息
    //超时最大重试次数
    protected int maxRetryCount = 3;//重试次数
    protected boolean isRetry = true;//是否重试
    protected boolean isEnable = true;//是否启动
    protected int retryCount = 0; //记录重试次数
    protected boolean isSuccess = false;
    protected long timeout = CxfSoaContants.WEBSERVICE_TEIMOUT;
    protected String buffMsg = null;
    protected String buffDetail = null;
    protected String exceptionMsg = "请求失败";
    protected String paramXml;

    public CnbmIntf getcnbmIntf() {
        return cnbmIntf;
    }

    public CnbmIntfLog getCnbmIntfLog() {
        return cnbmIntfLog;
    }

    protected CommonParam packageParams() {
        CommonParam params = null;
        try {
            params = this.getParams();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String errMsg = null;
        // 校验参数
        boolean valid = true;
        if (params == null) {
            valid = false;
            errMsg = "接口参数CommonParam不可为空";
        }
        if (StringUtils.isBlank(params.getIntfCode())) {
            valid = false;
            errMsg = "接口编码不可为空";
        }
        if (cnbmIntf == null) {
            //根据接口编码查询接口信息
            cnbmIntf = CnbmLogUtils.queryIntfInfo(params.getIntfCode());
            params.setUrl(cnbmIntf.getInputUrl());
            params.setMethodName(cnbmIntf.getInputMethodName());
            cnbmIntfLog.setIntfId(cnbmIntf.getId());
            cnbmIntfLog.setIntfName(cnbmIntf.getName());
            //接口是否开启
            if (CxfSoaContants.DISABLED_OR_FAILED.equals(cnbmIntf.getStatus())) {
                isEnable = false;
            }
            //是否重试
            if (CxfSoaContants.DISABLED_OR_FAILED.equals(cnbmIntf.getIsRetry())) {
                isRetry = false;
            }
            //重试次数
            if (cnbmIntf.getMaxRetryCount() != null) {
                maxRetryCount = cnbmIntf.getMaxRetryCount();
            }
            //超时时间
            if (cnbmIntf.getTimeout() != null) {
                timeout = cnbmIntf.getTimeout();
            }
        } else {
            params.setUrl(cnbmIntf.getInputUrl());
            params.setMethodName(cnbmIntf.getInputMethodName());
        }
        //检查接口开关设置
        if (!isEnable) {
            errMsg = "接口无法调用，接口开关设置为关";
            valid = false;
        } else if (StringUtils.isBlank(params.getUrl())) {
            valid = false;
            errMsg = "接口url不可为空";
        } else if (StringUtils.isBlank(params.getMethodName())) {
            valid = false;
            errMsg = "接口方法名不可为空";
        }
        if (!valid) {
            log.error("packageParams>params:{}", errMsg, params);
            this.recordIntfcLog(false, errMsg, errMsg, params, null);
            return null;
        }
        return params;
    }

    /**
     * 重试程序
     *
     * @param params
     * @param e
     * @return
     */
    protected boolean retryApp(CommonParam params, Exception e) {
        if (e.getCause() != null && e.getCause() instanceof SocketTimeoutException) {
            if (isRetry) {
                if (retryCount <= maxRetryCount) {
                    retryCount++;
                    return true;
                } else {
                    exceptionMsg = "请求超时，已重试三次";
                }
            } else {
                exceptionMsg = "请求超时，请再试一次";
            }
        } else {
            exceptionMsg = e.getClass().getName() + ":" + ExtStringUtils.excpStackTraceStr(e);
        }
        this.recordIntfcLog(false, e.getMessage(), exceptionMsg, params, paramXml);
        log.error("-##retryApp>接口调用发生异常！", e);
        return false;
    }

    protected class CommonInterfaceResult {
        private boolean success;
        private Object out;

        public CommonInterfaceResult(boolean success, Object out) {
            this.success = success;
            this.out = out;
        }

        public boolean isSuccess() {
            return success;
        }

        public Object getOut() {
            return out;
        }
    }

    /**
     * (接口监控)记录客户端出现异常的代码
     *
     * @param success   成功失败
     * @param logMsg    错误的LOG日志信息
     * @param logDetail 处理过后的错误可读信息
     * @param params    传入参数信息
     */
    protected void recordIntfcLog(boolean success, String logMsg, String logDetail, CommonParam params) {
        recordIntfcLog(success, logMsg, logDetail, params, null);
    }

    /**
     * (接口监控)记录客户端出现异常的代码
     *
     * @param success   成功失败
     * @param logMsg    错误的LOG日志信息
     * @param logDetail 处理过后的错误可读信息
     * @param params    传入参数信息
     * @param xmlParam  webservice xml格式化信息
     */
    protected void recordIntfcLog(boolean success, String logMsg, String logDetail, CommonParam params, String xmlParam) {
        this.fillMsgBuff(success, logMsg, logDetail);
        cnbmIntfLog.setInputStatus(success ? CxfSoaContants.ENABLE_OR_SUCCESS : CxfSoaContants.DISABLED_OR_FAILED);
        cnbmIntfLog.setRetryCount(retryCount);//记录重试次数
        if (StringUtils.isBlank(xmlParam)) {
            cnbmIntfLog.setInputParams(JSON.toJSONString(params.getParams()));
        } else {
            cnbmIntfLog.setInputParams(xmlParam);
        }
        if (!success) {
            cnbmIntfLog.setFailedReason(getResultMsg());
        }
        setCallback(this);
    }

    public String getResultMsg() {
        String result = this.buffMsg;
        if (!StringUtils.equals(this.buffMsg, this.buffDetail)) {
            result += "\n" + this.buffDetail;
        }
        return result;
    }

    private void fillMsgBuff(boolean isSuccess, String msg, String detail) {
        this.exceptionMsg = msg;
        this.buffMsg = msg;
        this.buffDetail = detail;
        this.isSuccess = isSuccess;
    }

    /**
     * 用于设置给controller赋值的
     *
     * @param bean
     */
    protected abstract void setCallback(CommonWebservice bean);

    /**
     * 获取接口需要参数
     *
     * @return Object[]
     */
    protected abstract CommonParam getParams() throws Exception;

    /**
     * 接口调用开关，子类可以重写此方法达到对每个接口的精确场景开关控制
     *
     * @return true表示调用开启，false表示调用关闭
     */
    protected void callOnOff(boolean isEnableIn) {
        this.isEnable = isEnableIn;
    }

}
