package com.cnbm.intf.core.controller;

import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.common.Result;
import com.cnbm.intf.core.common.utils.PageUtils;
import com.cnbm.intf.core.common.utils.Query;
import com.cnbm.intf.core.model.CnbmIntfLog;
import com.cnbm.intf.core.service.impl.CnbmIntfLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @create 2019022914:06
 */
@RestController
@RequestMapping("/intflog")
@Slf4j
public class CnbmIntfLogController extends BaseController {

    private final CnbmIntfLogServiceImpl cnbmIntfLogService;

    @Autowired
    public CnbmIntfLogController(CnbmIntfLogServiceImpl cnbmIntfLogService) {
        this.cnbmIntfLogService = cnbmIntfLogService;
    }

    /**
     * 根据接口id查询接口日志
     *
     * @param params 接收页面参数信息
     * @return
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) {
        Query<CnbmIntfLog> pageParams = new Query<>(params);
        PageUtils pageUtils = cnbmIntfLogService.queryPage(pageParams);
        return Result.success(pageUtils);
    }

    /**
     * 根据ID查询结果
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public Result get(@RequestParam String id) {
        return Result.success(cnbmIntfLogService.selectById(id));
    }



}
