package com.cnbm.intf.core.webservice;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口基础参数
 */
public class CommonParam {
    private String url; //webservice地址
    private String methodName; //webservice方法名
    private Object params; //webservice入口参数
    private boolean returnMap = true; //webservice是否返回Map封装类型标志，为false时需要指定cls属性
    @SuppressWarnings("rawtypes")
    private Class cls; //webservice返回结果封装类型，isReturnMap=false时必需

    private String intfCode; //接口编码

    private HttpMethod method = HttpMethod.POST;

    private MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;

    private Map<String, String> headerInfo = new HashMap<>();

    public String getIntfCode() {
        return intfCode;
    }

    public void setIntfCode(String intfCode) {
        this.intfCode = intfCode;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getParams() {
        return this.params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public boolean isReturnMap() {
        return this.returnMap;
    }

    public void setReturnMap(boolean returnMap) {
        this.returnMap = returnMap;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getCls() {
        return this.cls;
    }

    public <T> void setCls(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "CommonParam{" + "url='" + this.url + '\'' + ", methodName='" + this.methodName + '\'' + ", params="
                + this.params + ", returnMap=" + this.returnMap + ", cls=" + this.cls + '}';
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Map<String, String> getHeaderInfo() {
        return headerInfo;
    }

    public Map<String, String> setHeaderInfo(Map<String, String> headerInfo) {
        this.headerInfo = headerInfo;
        return headerInfo;
    }

    public Map<String, String> setHeaderInfo(String key, String vaule) {
        this.headerInfo.put(key, vaule);
        return headerInfo;
    }
}
