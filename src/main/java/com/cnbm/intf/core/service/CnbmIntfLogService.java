package com.cnbm.intf.core.service;

import com.cnbm.intf.core.common.service.IBaseService;
import com.cnbm.intf.core.model.CnbmIntfLog;

import java.util.List;
import java.util.Map;

/**
 *
 * @create 2019022914:07
 */
public interface CnbmIntfLogService extends IBaseService<CnbmIntfLog> {

    List<Map<String, Object>> queryToDayCount(String isFlag);

    Map<String,Object> queryToDayFailCount();
}
