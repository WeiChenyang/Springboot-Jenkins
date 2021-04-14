package com.cnbm.bs.client.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.cnbm.bs.client.demo2.gen.CommonServiceStub;
import com.cnbm.bs.server.demo1.pojo.Demo1;
import com.cnbm.bs.server.demo1.pojo.Demo1Out;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.utils.WebserviceResult;
import com.cnbm.intf.core.webservice.CommonInterfaceStrClient;
import com.cnbm.intf.core.webservice.CommonParam;
import com.cnbm.intf.core.webservice.CommonWebServiceClient;
import com.cnbm.intf.core.webservice.CommonWebservice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者调用生产者webservice
 *
 *
 * @create 201903110:06
 */
@RestController
public class Demo1Controller extends BaseController {

    /**
     * webservice使用一个字符串方式调用，里面存放xml的方式。优点改变参数不用去修改接口
     *
     * @param json
     * @return
     */
    @PostMapping("/api/demo1")
    public WebserviceResult demo1(@RequestBody String json) {
        Demo1 demo1 = JSON.parseObject(json, Demo1.class);
        String code = JSON.parseObject(json).getString("code");
        Map<String, String> resultMap = new HashMap<>();
        CommonInterfaceStrClient client = new CommonInterfaceStrClient() {
            @Override
            protected void setCallback(CommonWebservice bean) {
                setLog(bean);
            }

            @Override
            protected CommonParam getParams() throws Exception {
                CommonParam commonParam = new CommonParam();
                commonParam.setReturnMap(false);
                commonParam.setIntfCode(code);
                commonParam.setParams(demo1);
                commonParam.setCls(Demo1Out.class);
                return commonParam;
            }

            @Override
            protected boolean CommonReturnMessage(Map<String, Object> returnMessage) {
                return false;
            }

            @Override
            protected boolean CommonReturnMessage(Object returnObject) {
                Demo1Out result = (Demo1Out) returnObject;
                if (null != result) {
                    resultMap.put("result", JSON.toJSONString(result));
                    resultMap.put("isFlag", result.getIsFlag());
                    return true;
                } else {
                    resultMap.put("error", getResultMsg());
                }
                return false;
            }
        };
        boolean isSuccess = client.processInfo();
        if (!isSuccess) {
            resultMap.put("isFlag", "N");
            resultMap.put("error", client.getExceptionMsg());
        }
        //throw new ExecutorException("测试消费端报错");
        return WebserviceResult.resultData(resultMap);
    }

    /**
     * 这种是根据wsdl生成客户端代码进行调用，有点传递参数清晰
     * @param json
     * @return
     */
    @PostMapping("/api/demo2")
    public WebserviceResult demo2(@RequestBody String json) throws Exception {
        String code = JSON.parseObject(json).getString("code");
        String name = JSON.parseObject(json).getString("name");
        Map<String, String> resultMap = new HashMap<>();
        CommonWebServiceClient<CommonServiceStub> client = new CommonWebServiceClient<CommonServiceStub>() {
            @Override
            protected CommonParam getParams() throws Exception {
                CommonParam commonParam = new CommonParam();
                commonParam.setIntfCode(code);
                CommonServiceStub.SayHelloE sayHello = new CommonServiceStub.SayHelloE();
                CommonServiceStub.SayHello hello = new CommonServiceStub.SayHello();
                hello.setUserName(name);
                sayHello.setSayHello(hello);
                commonParam.setParams(sayHello);
                commonParam.setCls(CommonServiceStub.class);
                return commonParam;
            }

            @Override
            protected CommonInterfaceResult processResult(Object t) {
                CommonServiceStub.SayHelloResponseE result = (CommonServiceStub.SayHelloResponseE) t;
                System.out.println("返回结果:" + result.getSayHelloResponse().getString());
                resultMap.put("isFlag", "Y");
                resultMap.put("data", result.getSayHelloResponse().getString());
                return new CommonInterfaceResult(true, result.getSayHelloResponse().getString());
            }

            @Override
            protected void setCallback(CommonWebservice bean) {
                setLog(bean);
            }
        };
        boolean isSuccess = client.processInfo();
        if (!isSuccess) {
            resultMap.put("isFlag", "N");
            resultMap.put("error", client.getExceptionMsg());
        }
        //throw new Exception("dsdsd");
        return WebserviceResult.resultData(resultMap);
    }

}
