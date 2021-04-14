package com.cnbm.intf.core.common;

import com.cnbm.intf.core.model.CnbmIntf;
import com.cnbm.intf.core.model.CnbmIntfLog;
import com.cnbm.intf.core.webservice.CommonWebservice;

/**
 * 公共业务处理
 *
 *
 * @create 2019022914:04
 */
public class BaseController {

    protected CnbmIntfLog cnbmIntfLog;//日志信息
    protected CnbmIntf cnbmIntf;

    public CnbmIntfLog getCnbmIntfLog() {
        return cnbmIntfLog;
    }

    public void setCnbmIntfLog(CnbmIntfLog cnbmIntfLog) {
        this.cnbmIntfLog = cnbmIntfLog;
    }

    protected void setLog(CommonWebservice commonWebservice) {
        cnbmIntfLog = commonWebservice.getCnbmIntfLog();
        cnbmIntf = commonWebservice.getcnbmIntf();
    }

}
