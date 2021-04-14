package com.cnbm.intf.core.mapper;

import com.cnbm.intf.core.common.BaseCnbmMapper;
import com.cnbm.intf.core.model.CnbmIntf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @create 2019022914:00
 */
@Mapper
public interface CnbmIntfMapper extends BaseCnbmMapper<CnbmIntf> {
    List<Map<String, Object>> countOutProject();
}
