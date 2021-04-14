package com.cnbm.bs.server.demo1;

import com.cnbm.bs.server.demo1.pojo.Test1Bean;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @create 2019031023:36
 */
@WebService(name = "CommonService", // 暴露服务名称
        targetNamespace = "http://demo1.server.bs.cnbm.com"// 命名空间,一般是接口的包名倒序
)
public interface CommonService {

    @WebMethod
    @WebResult(name = "String", targetNamespace = "")
    public String sayHello(@WebParam(name = "userName") String name);

    @WebMethod
    @WebResult(name = "String", targetNamespace = "")
    public String test1(@WebParam(name = "bean") Test1Bean bean);
}
