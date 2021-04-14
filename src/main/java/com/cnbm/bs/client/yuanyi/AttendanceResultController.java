package com.cnbm.bs.client.yuanyi;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.common.Result;
import com.cnbm.intf.core.restful.CommonRestFullClient;
import com.cnbm.intf.core.utils.WebserviceResult;
import com.cnbm.intf.core.webservice.CommonParam;
import com.cnbm.intf.core.webservice.CommonWebservice;

@RestController
public class AttendanceResultController extends BaseController {
	/**
     * rest或者http
     *
     * @return
     */
	@RequestMapping("/api/apiGetResult")
    public WebserviceResult apiGetResult() throws Exception {
		JSONObject jsonCollection = new JSONObject();
        JSONArray personJsonArray = new JSONArray();
        JSONObject personJson = new JSONObject();
        JSONObject personGroupJson = new JSONObject();
		JSONObject param = new JSONObject();
		String code = "P_001";
		param.put("pageNum",1);
        param.put("pageSize",100000);
        personJson.put("name","魏晨阳");
        personJsonArray.add(personJson);
        personGroupJson.put("persons",personJsonArray);
        jsonCollection.put("startTime","2021-03-11 18:00:00");
        jsonCollection.put("endTime","2021-03-12 00:00:00");
        jsonCollection.put("personGroup",personGroupJson);
        param.put("entity",jsonCollection);
        Map<String, String> resultMap = new HashMap<>();
        //对方是map对象时需要使用MultiValueMap才可以
        CommonRestFullClient<Result, String> client = new CommonRestFullClient<Result, String>() {
            @Override
            protected CommonInterfaceResult processResult(Result s) {
            	resultMap.put("data", JSON.toJSONString(s.getData()));
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
                commonParam.setParams(param);
                commonParam.setMethod(HttpMethod.POST);//可以不用填写默认就是post
                commonParam.setMediaType(MediaType.APPLICATION_JSON);//请求上下文格式，表单还是json
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

    public static void main(String[] args){
	    JSONArray jsonArray = new JSONArray();
        JSONObject param = new JSONObject();
        param.put("code","YUANYI");
        param.put("method","POST");

        JSONObject jsonCollection = new JSONObject();
        jsonCollection.put("id","1");
        jsonCollection.put("ccuscode","001");
        jsonCollection.put("ccusname","客户");
        jsonCollection.put("define1","1");
        jsonCollection.put("define2","2");

        JSONObject jsonCollection2 = new JSONObject();
        jsonCollection2.put("id","2");
        jsonCollection2.put("ccuscode","002");
        jsonCollection2.put("ccusname","客户2");
        jsonCollection2.put("define1","3");
        jsonCollection2.put("define2","4");

        jsonArray.add(jsonCollection);
        jsonArray.add(jsonCollection2);
        param.put("data",jsonArray);
        String jsonStr = String.valueOf(param);
        System.out.println(jsonStr);
    }
}
