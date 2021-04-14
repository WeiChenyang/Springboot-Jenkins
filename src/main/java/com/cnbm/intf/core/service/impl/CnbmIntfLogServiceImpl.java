package com.cnbm.intf.core.service.impl;

import com.cnbm.intf.core.common.service.impl.BaseServiceImpl;
import com.cnbm.intf.core.mapper.CnbmIntfLogMapper;
import com.cnbm.intf.core.model.CnbmIntfLog;
import com.cnbm.intf.core.service.CnbmIntfLogService;
import com.cnbm.intf.core.utils.CxfSoaContants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 接口日志管理
 *
 *
 * @create 2019022914:24
 */
@Service
@Slf4j
public class CnbmIntfLogServiceImpl extends BaseServiceImpl<CnbmIntfLogMapper, CnbmIntfLog> implements CnbmIntfLogService {
    @Override
    public List<Map<String, Object>> queryToDayCount(String isFlag) {
        if (StringUtils.equals(isFlag, CxfSoaContants.DISABLED_OR_FAILED)) {
            isFlag = CxfSoaContants.DISABLED_OR_FAILED;
        } else {
            isFlag = CxfSoaContants.ENABLE_OR_SUCCESS;
        }
        return this.baseMapper.queryToDayCount(isFlag);
    }

    @Override
    public Map<String, Object> queryToDayFailCount() {
        return baseMapper.queryToDayFailCount();
    }


}
