package com.cnbm.intf.core.controller;

import com.cnbm.intf.core.common.Result;
import com.cnbm.intf.core.service.CnbmIntfLogService;
import com.cnbm.intf.core.service.CnbmIntfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 报表
 *
 *
 * @create 201903139:47
 */
@RestController
@RequestMapping("/api/report")
@Slf4j
public class CnbmReportController {
    @Autowired
    private CnbmIntfLogService logService;

    @Autowired
    private CnbmIntfService intfService;

    /**
     * 当天接口调用及成功统计
     *
     * @param isFlag 0成功 1失败
     * @return
     */
    @PostMapping("/getToDayCount")
    public Result getToDayCount(@RequestParam(value = "isFlag", required = false) String isFlag) {
        List<Map<String, Object>> maps = logService.queryToDayCount(isFlag);
        return Result.success(maps);
    }

    /**
     * 当天接口调用及失败统计
     *
     * @return
     */
    @PostMapping("/queryToDayFailCount")
    public Result queryToDayFailCount() {
        Map<String, Object> stringObjectMap = logService.queryToDayFailCount();
        return Result.success(stringObjectMap);
    }

    /**
     * 当天接口调用及失败统计
     *
     * @return
     */
    @PostMapping("/countOutProject")
    public Result countOutProject() {
        List<Map<String, Object>> cnbmIntfs = intfService.countOutProject();
        return Result.success(cnbmIntfs);
    }
}
