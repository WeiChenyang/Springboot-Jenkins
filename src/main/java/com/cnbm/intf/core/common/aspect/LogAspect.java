package com.cnbm.intf.core.common.aspect;

import com.alibaba.fastjson.JSON;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.model.CnbmIntfLog;
import com.cnbm.intf.core.service.CnbmIntfLogService;
import com.cnbm.intf.core.utils.CnbmLogUtils;
import com.cnbm.intf.core.utils.CxfSoaContants;
import com.cnbm.intf.core.utils.ExtStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 日志处理
 *
 *
 * @create 2019031022:26
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    @Autowired
    private CnbmIntfLogService cnbmIntfLogService;

    @Pointcut("execution(public * com.cnbm..*.*Controller.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("request start");
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        long startTime = 0;//开始时间
        Object result = null;
        long endTime = 0;
        String isProcess = CxfSoaContants.ENABLE_OR_SUCCESS;
        String throwableMsg = null;
        try {
            startTime = System.currentTimeMillis();
            // result的值就是被拦截方法的返回值
            result = pjp.proceed();
            endTime = System.currentTimeMillis();
        } catch (Throwable throwable) {
            log.error("执行错误", throwable);
            endTime = System.currentTimeMillis();
            isProcess = CxfSoaContants.DISABLED_OR_FAILED;
            throwableMsg = ExtStringUtils.excpStackTraceStr(throwable);
        }
        //时间差
        long responseTime = endTime - startTime;
        String ipAddr = CnbmLogUtils.getCliectIp(request);
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String params;
        if ("POST".equals(method)) {
            Object[] paramsArray = pjp.getArgs();
            params = argsArrayToString(paramsArray);
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            params = JSON.toJSONString(paramsMap);
        }
        Object controller = pjp.getTarget();
        if (controller instanceof BaseController) {
            BaseController baseController = (BaseController) controller;
            CnbmIntfLog cnbmIntfLog = baseController.getCnbmIntfLog();
            if (cnbmIntfLog != null) {
                cnbmIntfLog.setCreatedDate(new Date(startTime));
                cnbmIntfLog.setUpdatedDate(new Date(endTime));
                cnbmIntfLog.setOutputParams(params);
                cnbmIntfLog.setOutputStatus(isProcess);
                cnbmIntfLog.setIpAddress(ipAddr);
                cnbmIntfLog.setUrl(url);
                cnbmIntfLog.setMethodExt(method);
                long ss = responseTime / 1000;
                boolean type = true;
                if (ss != 0) {
                    responseTime = ss;
                    type = false;
                }
                cnbmIntfLog.setResponseTimeExt(Long.toString(responseTime) + (type ? "ms" : "s"));
                if (cnbmIntfLog.getInputStatus().equals(CxfSoaContants.ENABLE_OR_SUCCESS) && isProcess.equals(CxfSoaContants.ENABLE_OR_SUCCESS)) {
                    cnbmIntfLog.setStatus(CxfSoaContants.ENABLE_OR_SUCCESS);
                } else if (isProcess.equals(CxfSoaContants.DISABLED_OR_FAILED)) {
                    cnbmIntfLog.setFailedReason(throwableMsg);
                    cnbmIntfLog.setStatus(CxfSoaContants.DISABLED_OR_FAILED);
                } else if (cnbmIntfLog.getInputStatus().equals(CxfSoaContants.DISABLED_OR_FAILED)) {
                    cnbmIntfLog.setStatus(CxfSoaContants.DISABLED_OR_FAILED);
                }
                log.debug(cnbmIntfLog.toString());
                cnbmIntfLogService.insert(cnbmIntfLog);
            }
        }
        return result;
    }

    /**
     * 请求参数拼装
     *
     * @param paramsArray
     * @return
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                params.append(JSON.toJSONString(paramsArray[i])).append(" ");
            }
        }
        return params.toString().trim();
    }

}
