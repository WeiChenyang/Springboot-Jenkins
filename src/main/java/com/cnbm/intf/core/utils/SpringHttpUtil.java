package com.cnbm.intf.core.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @create 2019031216:57
 */
@Component
@Slf4j
public class SpringHttpUtil {

    @Autowired
    private HttpServletResponse response;


    @Autowired
    private HttpServletRequest request;


    /**
     * 获取用户请求头部或者cookie中的参数
     */
    public String getParams(String name) {
        String result = getHeader(name);
        //cookie
        if (StringUtils.isBlank(result)) {
            result = CookieUtils.getCookieValue(name);
        }
        return result;
    }

    public String getHeader(String name) {
        String result= request.getHeader(name);
        if(StringUtils.isBlank(result)){
            result=request.getParameter(name);
        }
        return result;
    }

    public void setHeader(Map<String, String> map) {
        if (map == null || map.isEmpty() || map.size() == 0)
            return;
        map.entrySet().stream().forEach(x -> {
            response.setHeader(x.getKey(), x.getValue());
        });

    }

    public void setCookie(Map<String, String> map) {
        if (map == null || map.isEmpty() || map.size() == 0)
            return;
        map.entrySet().stream().forEach(x -> {
            CookieUtils.setCookie(x.getKey(), x.getValue());
        });
    }
}
