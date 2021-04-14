package com.cnbm.intf.core.mapper;

import com.cnbm.intf.core.common.BaseCnbmMapper;
import com.cnbm.intf.core.model.CnbmIntfLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @create 2019022914:01
 */
@Mapper
public interface CnbmIntfLogMapper extends BaseCnbmMapper<CnbmIntfLog> {

    List<Map<String, Object>> queryToDayCount(@Param("isFlag") String isFlag);

    Map<String,Object> queryToDayFailCount();
}
