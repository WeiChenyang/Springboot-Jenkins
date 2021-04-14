package com.cnbm.intf.core.utils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cnbm.intf.core.model.CnbmIntf;
import com.cnbm.intf.core.model.CnbmIntfLog;
import com.cnbm.intf.core.service.CnbmIntfLogService;
import com.cnbm.intf.core.service.CnbmIntfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * 日志公共类
 *
 *
 * @create 2019031018:08
 */
@Component
public class CnbmLogUtils {

    private static CnbmLogUtils bean;

    @Autowired
    private CnbmIntfService cnbmIntfService;

    @Autowired
    private CnbmIntfLogService cnbmIntfLogService;


    @PostConstruct
    public void init() {
        bean = this;
    }


    public static void saveInftLog(CnbmIntfLog cnbmIntfLog) {
        bean.cnbmIntfLogService.insertExt(cnbmIntfLog);
    }

    public static CnbmIntf queryIntfInfo(String intfCode) {
        EntityWrapper<CnbmIntf> wrapper = new EntityWrapper<>();
        wrapper.eq("code", intfCode);
        return bean.cnbmIntfService.selectOne(wrapper);
    }

    /**
     * 获取客户端ip地址
     * @param request
     * @return
     */
    public static String getCliectIp(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

}
