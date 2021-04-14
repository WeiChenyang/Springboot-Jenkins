package com.cnbm.bs.client.demo3;

import com.alibaba.fastjson.JSON;
import com.cnbm.intf.core.common.Result;
import com.cnbm.intf.core.common.utils.PageUtils;
import com.cnbm.intf.core.common.utils.Query;
import com.cnbm.intf.core.model.CnbmIntfLog;
import com.cnbm.intf.core.service.impl.CnbmIntfLogServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 */
@RestController
@RequestMapping("/api/test")
public class Test2Controller {

    @Autowired
    private CnbmIntfLogServiceImpl cnbmIntfLogService;

    /**
     * 根据接口id查询接口日志
     *
     * @param json 接收页面参数信息
     * @return
     */
    @PostMapping("/list")
    public Result list(@RequestBody String json) {
        Map map = new HashMap();
        if (StringUtils.isNotBlank(json)) {
            map = JSON.parseObject(json, Map.class);
        }
        Query<CnbmIntfLog> pageParams = new Query<>(map);
        PageUtils pageUtils = cnbmIntfLogService.queryPage(pageParams);
        return Result.success(pageUtils);
    }
}
