package com.cnbm.intf.core.service;

import com.cnbm.intf.core.common.service.IBaseService;
import com.cnbm.intf.core.model.CnbmIntf;

import java.util.List;
import java.util.Map;

/**
 *
 * @create 2019022914:07
 */
public interface CnbmIntfService extends IBaseService<CnbmIntf> {
    List<Map<String, Object>> countOutProject();
}
