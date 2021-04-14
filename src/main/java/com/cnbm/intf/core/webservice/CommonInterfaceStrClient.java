package com.cnbm.intf.core.webservice;

import com.cnbm.intf.core.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import java.util.Map;
import java.util.Properties;

/**
 * webservice作为调用端公共方法
 */
@Slf4j
public abstract class CommonInterfaceStrClient extends CommonWebservice {

    static {
        Properties props = System.getProperties();
        props.setProperty("org.apache.cxf.stax.allowInsecureParser", "1");
        props.setProperty("UseSunHttpHandler", "true");
    }


    /**
     * 参数校验
     *
     * @return
     */
    private boolean validateParam() {
        String errMsg = null;
        CommonParam params = packageParams();
        log.info("-##validateParam:{}", params);
        if (params != null) {
            //将CommonParam中object参数转为xml格式
            paramXml = XmlUtil.objectToXml(params.getParams());
        } else {
            return false;
        }
        // 校验参数
        boolean valid = true;
        if (!params.isReturnMap() && params.getCls() == null) {
            valid = false;
            errMsg = "接口返回非Map封装时(returnMap=false)，必须指定封装类型cls";
        }
        if (!valid) {
            log.error("-##validateParam>params:{}", errMsg, params);
            this.recordIntfcLog(false, errMsg, errMsg, params, paramXml);
            return false;
        }
        log.info("接口参数xml:{}", paramXml);
        return true;
    }


    /**
     * 客户端接口公共处理方法
     * 外面直接调用这个方法，这个接口中已经包涵了实现方法，去实现两个接口
     * 1.getParams 获取接口需要参数
     * 2.CommonReturnMessage 将返回结果传递给实现方法
     */
    public boolean processInfo() {
        String errMsg = null;
        CommonParam params = null;
        if (retryCount == 0) {
            boolean isSuccess = validateParam();
            if (!isSuccess) {
                return false;
            }
        }

        try {
            params = packageParams();
            //创建webservice请求实例
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            //设置webservice地址
            Client client = dcf.createClient(params.getUrl());
            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            HTTPClientPolicy policy = new HTTPClientPolicy();
            policy.setConnectionTimeout(timeout);
            policy.setReceiveTimeout(timeout);
            conduit.setClient(policy);
            // 需要密码的情况需要加上用户名和密码
            // client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME,
            // PASS_WORD));
            Object[] objects = client.invoke(params.getMethodName(), paramXml);
            if (objects == null || objects.length == 0 || objects[0] == null) {
                errMsg = "接口返回值为空";
                log.error("-##processInfo>{}\n-##processInfo>param xml:{}", errMsg, paramXml);
                this.recordIntfcLog(false, errMsg, errMsg, params, paramXml);
                return false;
            }
            String resultStr = objects[0].toString();
            cnbmIntfLog.setInputReturnVal(resultStr);
            log.info("-##processInfo>接口返回内容:{}", resultStr);
            boolean returnBoolean = false; //接口调用成功返回标志
            if (params.isReturnMap()) {
                Map<String, Object> resultMap = XmlUtil.xmlToMap(resultStr);
                log.info("-##processInfo>接口返回值map封装:{}", resultMap);
                returnBoolean = this.CommonReturnMessage(resultMap);
            } else {
                Object resultObj = XmlUtil.xmlToObject(resultStr, params.getCls());
                log.info("-##processInfo>接口返回值object封装:{}" + resultObj);
                returnBoolean = this.CommonReturnMessage(resultObj);
            }
            log.info("-##processInfo>接口调用完成标志:{}", returnBoolean);
            this.recordIntfcLog(returnBoolean, "接口调用完成", resultStr, params, paramXml);
            return returnBoolean;
        } catch (Exception e) {
            if (retryApp(params, e)) return processInfo();
        }
        return false;
    }

    //请求异常信息
    public String getExceptionMsg() {
        return exceptionMsg;
    }



    /**
     * 将返回结果传递给实现方法
     *
     * @param returnMessage 接口返回的数据
     * @return 处理成功还是失败 true 成功 false失败
     */
    protected abstract boolean CommonReturnMessage(Map<String, Object> returnMessage);

    /**
     * 将返回结果传递给实现方法
     *
     * @param returnObject 接口返回的数据
     * @return 处理成功还是失败 true 成功 false失败
     */
    protected abstract boolean CommonReturnMessage(Object returnObject);


    protected boolean isSuccess() {
        return isSuccess;
    }

}
