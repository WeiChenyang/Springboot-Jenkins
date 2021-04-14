package com.cnbm.bs.server.demo1;

import com.cnbm.bs.server.demo1.pojo.Demo1;
import com.cnbm.bs.server.demo1.pojo.Demo1Out;
import com.cnbm.bs.server.demo1.pojo.Test1Bean;
import com.cnbm.intf.core.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.xml.bind.JAXBException;

/**
 *
 * @create 2019031023:40
 */
@WebService(serviceName = "CommonService", // 与接口中指定的name一致
        targetNamespace = "http://demo1.server.bs.cnbm.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.cnbm.bs.server.demo1.CommonService"// 接口地址
)
@Component
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Override
    public String sayHello(String xml) {
        System.out.println("-----------------------------------------diaoyongle");
        Demo1 demo1 = null;
        Demo1Out demo1Out = new Demo1Out();
        try {
            demo1 = XmlUtil.xmlToObject(xml, Demo1.class);

        } catch (JAXBException e) {
            log.error("转换异常---------------------------");
            demo1Out.setIsFlag("N");
        }
        demo1Out.setIsFlag("Y");
        return XmlUtil.objectToXml(demo1Out);
    }

    @Override
    public String test1(Test1Bean bean) {
        return bean.getUserName() + bean.getAge();
    }
}
