package com.cnbm.bs.server.config;

import com.cnbm.bs.server.demo1.CommonService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * cxf服务端发布
 *
 * @create 2019030919:54
 */
@Configuration
public class CxfConfig {
    @Autowired
    private CommonService commonService;

    @Bean
    public ServletRegistrationBean disServlet() {
        return new ServletRegistrationBean(new CXFServlet(),"/webservice/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint endpointCommonService() {
        EndpointImpl endpoint = new EndpointImpl(springBus(),commonService);
        endpoint.publish("/api/commonService");
        return endpoint;
    }
}
