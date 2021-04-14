package com.cnbm.intf.core.common.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnbm.intf.core.model.CnbmIntf;
import com.cnbm.intf.core.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @create 2019031211:13
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private SpringHttpUtil httpUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        //IP校验
        String ipAddr = CnbmLogUtils.getCliectIp(request);
        log.info("请求地址：{},IP:{},Token:{}", request.getRequestURI(), request.getRemoteAddr(), token);
        if (StringUtils.isBlank(token)) {
            token = httpUtil.getParams("token");
            if (StringUtils.isBlank(token)) {
                responseResult(response, "无权限访问", true);
                return false;
            }
        } else {
            Map<String, String> header = new HashMap<>();
            header.put("token", StringUtils.deleteWhitespace(token));
            httpUtil.setCookie(header);
        }
        try {
            //校验token时间
            String tokenDecry = null;
            try {
                tokenDecry = EncryptUtil.buildRSADecryptByPrivateKey(token, KeyUtils.privateKey);
            } catch (Exception e) {
                tokenDecry = EncryptUtil.buildRSADecryptByPrivateKey(URLDecoder.decode(token, "UTF-8"), KeyUtils.privateKey);
            }
            JSONObject tokenJson = JSON.parseObject(tokenDecry);
            boolean isAdmin = tokenJson.getBooleanValue("isAdmin");
            request.setAttribute("name", tokenJson.getString("name"));
            request.setAttribute("isAdmin", tokenJson.getString("isAdmin"));
            if (isAdmin) {
                return true;
            }
            Date date = tokenJson.getDate("t");//获取时间
            String code = tokenJson.getString("cnbmIntfCode");//获取接口编码
            long startDate = date.getTime();
            long endDate = System.currentTimeMillis();
            long time = (endDate - startDate) / DateUtils.MILLIS_PER_MINUTE;
            CnbmIntf cnbmIntf = CnbmLogUtils.queryIntfInfo(code);
            //IP校验
            if (null != cnbmIntf && StringUtils.isNotBlank(cnbmIntf.getIpAddress())) {
                String ipAddress = cnbmIntf.getIpAddress();
                try {
                    String[] ips = ipAddress.split(",");
                    boolean isTrue = false;
                    for (String ip : ips) {
                        String regIp = ip.replaceAll("\\.", "\\.").replaceAll("\\*", "(\\.|\\d)*");
                        Pattern pattern = Pattern.compile(regIp);
                        Matcher matcher = pattern.matcher(ipAddr);
                        if (matcher.matches()) {
                            isTrue = true;
                            break;
                        }
                    }
                    if (!isTrue) {
                        responseResult(response, "该IP无权访问", false);
                        return false;
                    }
                } catch (Exception e) {
                    responseResult(response, "IP校验失败，" + ExtStringUtils.excpStackTraceStr(e), false);
                    return false;
                }
            }
            log.debug("code:{},t:{},time:{},url:{}", code, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(startDate), time, request.getRequestURI());
            if (time <= 20) {
                return true;
            }
            responseResult(response, "超时", true);
        } catch (Exception e) {
            log.error("校验异常：" + e);
            responseResult(response, "校验异常", true);
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /**
     * 获取请求Body
     *
     * @param request request
     * @return String
     */
    public String getBodyString(HttpServletRequest request) {
        try {
            return inputStream2String(request.getInputStream());
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将inputStream里的数据读取出来并转换成字符串
     *
     * @param inputStream inputStream
     * @return String
     */
    private String inputStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }

        return sb.toString();
    }

    private void responseResult(HttpServletResponse response, String result, boolean isRedirect) throws IOException {
        response.setCharacterEncoding("UTF-8");
        if (isRedirect) {
            response.setStatus(404);
            response.sendRedirect("/cnbm/login");
        }
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

}
