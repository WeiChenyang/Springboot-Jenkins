package com.cnbm.bs.client.test1;

import com.alibaba.fastjson.JSON;
import com.cnbm.bs.client.test1.gen.CommonServiceStub;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.utils.WebserviceResult;
import com.cnbm.intf.core.webservice.CommonParam;
import com.cnbm.intf.core.webservice.CommonWebServiceClient;
import com.cnbm.intf.core.webservice.CommonWebservice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @create 2019031314:08
 */
@RestController
public class Test1Controller extends BaseController {

    /**
     * 这种是根据wsdl生成客户端代码进行调用，有点传递参数清晰
     *
     * @param json
     * @return
     */
    @PostMapping("/api/test1")
    public WebserviceResult test1(@RequestBody String json) throws Exception {
        String code = JSON.parseObject(json).getString("code");
        String userName = JSON.parseObject(json).getString("userName");
        String age = JSON.parseObject(json).getString("age");
        Map<String, String> resultMap = new HashMap<>();
        CommonWebServiceClient<CommonServiceStub> client = new CommonWebServiceClient<CommonServiceStub>() {
            @Override
            protected CommonParam getParams() throws Exception {
                CommonParam commonParam = new CommonParam();
                commonParam.setIntfCode(code);
                commonParam.setMethodName("test1");
                CommonServiceStub.Test1E test1E = new CommonServiceStub.Test1E();
                CommonServiceStub.Test1 test1 = new CommonServiceStub.Test1();
                CommonServiceStub.Test1Bean test1Bean = new CommonServiceStub.Test1Bean();
                test1Bean.setUserName(userName);
                test1Bean.setAge(age);
                test1.setBean(test1Bean);
                test1E.setTest1(test1);
                commonParam.setParams(test1E);
                commonParam.setCls(CommonServiceStub.class);
                return commonParam;
            }

            @Override
            protected CommonWebServiceClient.CommonInterfaceResult processResult(Object t) {
                CommonServiceStub.Test1ResponseE result = (CommonServiceStub.Test1ResponseE) t;
                System.out.println("返回结果:" + result.getTest1Response().getString());
                resultMap.put("isFlag", "Y");
                resultMap.put("data", result.getTest1Response().getString());
                return new CommonInterfaceResult(true, result.getTest1Response().getString());
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
