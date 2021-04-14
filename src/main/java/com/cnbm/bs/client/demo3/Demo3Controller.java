package com.cnbm.bs.client.demo3;

import com.alibaba.fastjson.JSON;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.common.Result;
import com.cnbm.intf.core.restful.CommonRestFullClient;
import com.cnbm.intf.core.utils.WebserviceResult;
import com.cnbm.intf.core.webservice.CommonParam;
import com.cnbm.intf.core.webservice.CommonWebservice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @create 2019031315:47
 */
@RestController
public class Demo3Controller extends BaseController {


    /**
     * rest或者http
     *
     * @param json
     * @return
     */
    @PostMapping("/api/demo3")
    public WebserviceResult demo3(@RequestBody String json) throws Exception {
        String code = JSON.parseObject(json).getString("code");
        String isFlag = JSON.parseObject(json).getString("isFlag");
        Map<String, String> resultMap = new HashMap<>();
        //对方是map对象时需要使用MultiValueMap才可以
        CommonRestFullClient<Result, String> client = new CommonRestFullClient<Result, String>() {
            @Override
            protected CommonInterfaceResult processResult(Result s) {
                return new CommonInterfaceResult(true, JSON.toJSONString(s.getData()));
            }

            @Override
            protected void setCallback(CommonWebservice bean) {
                setLog(bean);
            }

            @Override
            protected CommonParam getParams() throws Exception {
                CommonParam commonParam = new CommonParam();
                commonParam.setIntfCode(code);
                commonParam.setCls(Result.class);
                commonParam.setParams(isFlag);
                commonParam.setHeaderInfo("token","GksOmpv4ugxdUXkNT49f1I6JL8715m5poSftQ4qd9W/XRP1vIp/oTtpz2GVvzTZ1crsuFt95g3FoTWYj9f+ZwnIP+ExLWq463xJEmbRoJrgtW9wdXJp0lvC0YMhQO3YKQ+FaEM4AP7fmNYBGH+cc3oSJUCCbz8CEqpc+FdSjJ1zJx6weFs5M6nFFfBMB26uXSwAH8JGL5uMNOEvwpJSOcnPFd4R6oiFpNX7E3Iuue4tjMWgIVnp2ICNbxwLjQswAPN19chHKKTqSzNSWw==");
                commonParam.setMethod(HttpMethod.POST);//可以不用填 "D8w9B5ni20SAe184uLVanv9nZPsnUU/1etvCqR2and1C/xqcyhS/NhI3EzBIQ6idW2aPg写默认就是post
                commonParam.setMediaType(MediaType.APPLICATION_FORM_URLENCODED);//请求上下文格式，表单还是json
                return commonParam;
            }
        };


        boolean isSuccess = client.processInfo();
        if (!isSuccess) {
            resultMap.put("isFlag", "N");
            resultMap.put("error", client.getResultMsg());
        }
        //throw new Exception("dsdsd");
        return WebserviceResult.resultData(resultMap);
    }

    /**
     * rest或者http get
     * 第一种方式：
     * http://localhost:8088/cnbm/intf/get?id=1 在commonParam.setParams("name=zhangsan");直接赋值
     * http://localhost:8088/cnbm/intf/get 在commonParam.setParams("name=zhangsan");直接赋值
     * 第二种方式:
     * http://localhost:8088/cnbm/intf/get
     * Map<String, String> params = new HashMap<>();
     * params.put("id", "1");
     * 在commonParam.setParams(params);直接赋值时使用Map对象,
     * http://localhost:8088/cnbm/intf/get?id=1
     * Map<String, String> params = new HashMap<>();
     * *   params.put("id", "1");
     * * 在commonParam.setParams(params);直接赋值时使用Map对象,
     *
     * @param json
     * @return
     */
    @PostMapping("/api/demo4")
    public WebserviceResult demo4(@RequestBody String json) throws Exception {
        String code = JSON.parseObject(json).getString("code");
        String id = JSON.parseObject(json).getString("id");
        Map<String, String> resultMap = new HashMap<>();
        //对方是map对象时需要使用MultiValueMap才可以
        CommonRestFullClient<Result, String> client = new CommonRestFullClient<Result, String>() {
            @Override
            protected CommonInterfaceResult processResult(Result s) {
                resultMap.put("data", JSON.toJSONString(s.getData()));
                return new CommonInterfaceResult(true, s.getData());
            }

            @Override
            protected void setCallback(CommonWebservice bean) {
                setLog(bean);
            }

            @Override
            protected CommonParam getParams() throws Exception {
                CommonParam commonParam = new CommonParam();
                commonParam.setIntfCode(code);
                commonParam.setCls(Result.class);
                commonParam.setParams("id=" + id);//get如果不是自己封装参数，那么就需要使用Map对象方式，系统自动会封装
                commonParam.setHeaderInfo("token", "D8w9B5ni20SAe184uLVanv9nZPsnUU/1etvCqR2and1C/xqcyhS/NhI3EzBIQ6idW2aPgGksOmpv4ugxdUXkNT49f1I6JL8715m5poSftQ4qd9W/XRP1vIp/oTtpz2GVvzTZ1crsuFt95g3FoTWYj9f+ZwnIP+ExLWq463xJEmbRoJrgtW9wdXJp0lvC0YMhQO3YKQ+FaEM4AP7fmNYBGH+cc3oSJUCCbz8CEqpc+FdSjJ1zJx6weFs5M6nFFfBMB26uXSwAH8JGL5uMNOEvwpJSOcnPFd4R6oiFpNX7E3Iuue4tjMWgIVnp2ICNbxwLjQswAPN19chHKKTqSzNSWw==");
                commonParam.setMethod(HttpMethod.GET);//可以不用填写默认就是post
                commonParam.setMediaType(MediaType.APPLICATION_FORM_URLENCODED);//请求上下文格式，表单还是json
                return commonParam;
            }
        };


        boolean isSuccess = client.processInfo();
        if (!isSuccess) {
            resultMap.put("isFlag", "N");
            resultMap.put("error", client.getResultMsg());
        }
        //throw new Exception("dsdsd");
        return WebserviceResult.resultData(resultMap);
    }
}
