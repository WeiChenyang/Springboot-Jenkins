package com.cnbm.bs.client.yuanyi;

import com.alibaba.fastjson.JSONObject;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.restful.CommonRestFullClient;
import com.cnbm.intf.core.utils.WebserviceResult;
import com.cnbm.intf.core.webservice.CommonParam;
import com.cnbm.intf.core.webservice.CommonWebservice;
import com.cnbm.sdk.util.CnbmEncryptUtil;
import com.cnbm.sdk.vo.EncryptBean;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSON;

@RestController
@Log4j
public class YuanyiController extends BaseController {

    /**
     * rest或者http
     *
     *
     * @param json
     * @return
     */
    @PostMapping("/api/yuanyi")
    public WebserviceResult yuanyiInterface(@RequestBody String json) throws Exception {
        log.info("======接收信息："+json);
        String code = JSON.parseObject(json).getString("code");
        String dataJsonStr = JSON.parseObject(json).getString("data");
        JSONObject dataJson = JSON.parseObject(dataJsonStr);
        log.info("======传输参数："+dataJson);

        Map<String, String> resultMap = new HashMap<>();
        //对方是map对象时需要使用MultiValueMap才可以
        CommonRestFullClient<ResultYuanYiU8, String> client = new CommonRestFullClient<ResultYuanYiU8, String>() {
            @Override
            protected CommonInterfaceResult processResult(ResultYuanYiU8 s) {
                return new CommonInterfaceResult(true, JSON.toJSONString(s.getMsg()));
            }
            @Override
            protected void setCallback(CommonWebservice bean) {
                setLog(bean);
            }

            @Override
            protected CommonParam getParams() throws Exception {
                EncryptBean encryptBean = new EncryptBean(code);
                String tokenValue = CnbmEncryptUtil.setheaderEncrypt(encryptBean); //获取接口TOKEN
                CommonParam commonParam = new CommonParam();
                commonParam.setIntfCode(code);
                commonParam.setCls(ResultYuanYiU8.class);
                commonParam.setParams(dataJson);
                commonParam.setHeaderInfo("token",tokenValue);
                commonParam.setMethod(HttpMethod.POST);//可以不用填 "D8w9B5ni20SAe184uLVanv9nZPsnUU/1etvCqR2and1C/xqcyhS/NhI3EzBIQ6idW2aPg写默认就是post
                commonParam.setMediaType(MediaType.APPLICATION_JSON_UTF8);//请求上下文格式，表单还是json
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
 /*public static void main(String[] args) {
        JSONObject jsonCollection = new JSONObject();
        JSONObject param = new JSONObject();
        param.put("code","YUANYI");
        param.put("method","POST");
        jsonCollection.put("id","1");
        jsonCollection.put("ccuscode","001");
        jsonCollection.put("ccusname","客户");
        jsonCollection.put("define1","1");
        jsonCollection.put("define2","2");
        param.put("data",jsonCollection);
        System.out.println(param);
        String paramStr = String.valueOf(param);
        String code = JSON.parseObject(paramStr).getString("code");
        String dataJsonStr = JSON.parseObject(paramStr).getString("data");
        System.out.println(code);
        System.out.println(dataJsonStr);
        JSONObject dataJson = JSON.parseObject(dataJsonStr);
        System.out.println(dataJson);
    }*/
}
