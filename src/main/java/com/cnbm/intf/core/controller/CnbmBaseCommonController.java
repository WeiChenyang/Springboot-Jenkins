package com.cnbm.intf.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.restful.CommonRestFullClient;
import com.cnbm.intf.core.utils.WebserviceResult;
import com.cnbm.intf.core.webservice.CommonParam;
import com.cnbm.intf.core.webservice.CommonWebservice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * rest请求公共处理
 */
@RestController
@RequestMapping("/api/common/intf")
public class CnbmBaseCommonController extends BaseController {

    /**
     * rest请求公共处理
     *
     * @param json
     * @return
     */
    @PostMapping("/postman")
    public WebserviceResult postman(@RequestBody String json) throws Exception {
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        String method = jsonObject.getString("method");
        String data = jsonObject.getJSONObject("data").toJSONString();
        Map<String, String> resultMap = new HashMap<>();
        //对方是map对象时需要使用MultiValueMap才可以
        CommonRestFullClient<String, String> client = new CommonRestFullClient<String, String>() {
            @Override
            protected CommonWebservice.CommonInterfaceResult processResult(String s) {
                if (StringUtils.isNotBlank(s)) {
                    Map jsonObject = JSON.parseObject(s, Map.class);
                    resultMap.putAll(jsonObject);
                }
                return new CommonWebservice.CommonInterfaceResult(true, resultMap);
            }

            @Override
            protected void setCallback(CommonWebservice bean) {
                setLog(bean);
            }

            @Override
            protected CommonParam getParams() throws Exception {
                CommonParam commonParam = new CommonParam();
                commonParam.setReturnMap(false);
                commonParam.setIntfCode(code);
                commonParam.setCls(String.class);
                commonParam.setParams(data);
                commonParam.setMethod(StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), method) ? HttpMethod.GET : HttpMethod.POST);//可以不用填写默认就是post
                commonParam.setMediaType(MediaType.APPLICATION_JSON_UTF8);//请求上下文格式，表单还是json
                return commonParam;
            }
        };

        boolean isSuccess = client.processInfo();
        if (!isSuccess) {
            resultMap.put("isFlag", "N");
            resultMap.put("error", client.getResultMsg());
        }
        return WebserviceResult.resultData(resultMap);
    }
}
