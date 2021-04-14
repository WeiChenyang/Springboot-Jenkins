package com.cnbm.intf.core.service.impl;

import com.cnbm.intf.core.common.service.impl.BaseServiceImpl;
import com.cnbm.intf.core.mapper.CnbmIntfMapper;
import com.cnbm.intf.core.model.CnbmIntf;
import com.cnbm.intf.core.service.CnbmIntfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 接口管理实现
 *
 *
 * @create 2019022914:08
 */
@Service
@Slf4j
public class CnbmIntfServiceImpl extends BaseServiceImpl<CnbmIntfMapper, CnbmIntf> implements CnbmIntfService {


    @Override
    public List<Map<String, Object>> countOutProject() {
        return baseMapper.countOutProject();
    }
}
