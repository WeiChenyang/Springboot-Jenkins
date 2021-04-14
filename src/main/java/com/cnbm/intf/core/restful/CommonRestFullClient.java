package com.cnbm.intf.core.restful;

import com.alibaba.fastjson.JSON;
import com.cnbm.intf.core.webservice.CommonParam;
import com.cnbm.intf.core.webservice.CommonWebservice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Map;

/**
 * restFul公共类
 *
 *
 * @create 2019031314:49
 */
@Slf4j
public abstract class CommonRestFullClient<T, O> extends CommonWebservice {

    public boolean processInfo() {
        CommonParam params = packageParams();
        log.info("-##processInfo>CommonParam:{}", params);
        if (params == null) {
            return false;
        }
        try {
            params = packageParams();
            //配置rest
            RestTemplate restTemplate = new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofMillis(super.timeout))
                    .setReadTimeout(Duration.ofMillis(super.timeout))
                    .build();
            //设置 Header
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(params.getMediaType());
            Map<String, String> inputHeader = params.getHeaderInfo();
            for (Map.Entry<String, String> stringStringEntry : inputHeader.entrySet()) {
                httpHeaders.set(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
            ResponseEntity<T> resp;
            if (params.getMethod().equals(HttpMethod.GET)) {
                UriComponentsBuilder uriComponents = UriComponentsBuilder.fromUriString(
                        params.getUrl());
                Object param = params.getParams();
                if (param == null) {
                    log.info("接口编码:{},接口名称：{},URL:{},没有传递参数", params.getIntfCode(), cnbmIntfLog.getIntfName(), params.getUrl());
                } else if (param instanceof Map) {
                    uriComponents.queryParams((MultiValueMap<String, String>) param);
                } else if (param instanceof String) {
                    uriComponents.query((String) param);
                } else {
                    CommonRestFullClient.this.recordIntfcLog(false, "未按规范要求开发", "get 请求参数选择String类型，就必须拼装好，例如：id=1&name=zhangsan 或者?id=1&name=lisi", params);
                    return false;
                }
                // 处理header信息
                for (Map.Entry<String, String> header : params.getHeaderInfo().entrySet()) {
                    uriComponents.queryParam(header.getKey(), URLEncoder.encode(header.getValue(), "UTF-8"));
                }
                URI uri = uriComponents.build().encode().toUri();
                log.debug("接口编码:{},接口名称：{},URL:{}", cnbmIntf.getCode(), cnbmIntf.getName(), uri.toString());
                resp = restTemplate.getForEntity(uri, params.getCls());
            } else {
                //设置参数
                HttpEntity<O> requestEntity = new HttpEntity<>((O) params.getParams(), httpHeaders);
                //执行请求
                resp = restTemplate
                        .exchange(params.getUrl(), params.getMethod(), requestEntity, params.getCls());
            }

            //返回请求返回值
            cnbmIntfLog.setInputReturnVal(JSON.toJSONString(resp.getBody()));
            log.info("接口编码:{},接口名称：{},返回结果:{}",cnbmIntf.getCode(), cnbmIntf.getName(),resp.getBody());
            CommonInterfaceResult interfaceResult = processResult(resp.getBody());
            //如果对方直接返回null就报异常，没有按规范写
            if (interfaceResult == null) {
                this.recordIntfcLog(false, "未按规范要求开发", "processResult返回null，请按照CommonInterfaceResult对象填写信息", params);
                return false;
            }
            //返回请求返回值
            cnbmIntfLog.setInputReturnVal(JSON.toJSONString(interfaceResult.getOut()));
            this.recordIntfcLog(interfaceResult.isSuccess(), "", "", params);
            return true;
        } catch (Exception e) {
            if (retryApp(params, e)) return processInfo();
        }
        return false;
    }

    protected abstract CommonInterfaceResult processResult(T t);

    private class PackageUrl {
        private boolean myResult;
        private CommonParam params;
        private StringBuffer sb;

        public PackageUrl(CommonParam params) {
            this.params = params;
        }

        boolean is() {
            return myResult;
        }

        public StringBuffer getSb() {
            return sb;
        }

        public PackageUrl invoke() {
            //处理参数拼装到url中
            String url = params.getUrl();
            String index = StringUtils.indexOf(url, "?") > 0 ? null : "?";
            sb = new StringBuffer(url);
            if (StringUtils.isNotBlank(index)) {
                sb.append(index);
            }
            Object param = params.getParams();
            if (param == null) {
                log.info("接口编码:{},URL:{},没有传递参数", params.getIntfCode(), params.getUrl());
                return this;
            }
            if (param instanceof Map) {
                Map paramMap = (Map) param;
                boolean isTrue = true;
                for (Object o : paramMap.keySet()) {
                    if (StringUtils.isBlank(index) && isTrue) {
                        sb.append(o).append("=").append(paramMap.get(o));
                        isTrue = false;
                    } else {
                        sb.append("&").append(o).append("=").append(paramMap.get(o));
                    }
                }

            } else if (param instanceof String) {
                if (StringUtils.isNotBlank(index)) {
                    sb.append("&");
                }
                sb.append(param);
            } else {
                CommonRestFullClient.this.recordIntfcLog(false, "未按规范要求开发", "get 请求参数选择String类型，就必须拼装好，例如：id=1&name=zhangsan 或者?id=1&name=lisi", params);
                myResult = true;
                return this;
            }
            // 处理header信息
            for (Map.Entry<String, String> stringStringEntry : params.getHeaderInfo().entrySet()) {
                sb.append("&").append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue());
            }
            myResult = false;
            return this;
        }
    }
}
