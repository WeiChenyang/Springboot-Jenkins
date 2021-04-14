package com.cnbm.intf.core.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cnbm.intf.core.common.BaseController;
import com.cnbm.intf.core.common.Result;
import com.cnbm.intf.core.common.utils.PageUtils;
import com.cnbm.intf.core.common.utils.Query;
import com.cnbm.intf.core.model.CnbmIntf;
import com.cnbm.intf.core.service.CnbmIntfService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 接口管理
 *
 *
 * @create 2019022914:03
 */
@RestController
@RequestMapping("/intf")
@Slf4j
public class CnbmIntfController extends BaseController {

    @Autowired
    private CnbmIntfService cnbmIntfService;

    /**
     * 查询接口信息
     *
     * @param params 接收页面参数信息
     * @return
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) {
        Query<CnbmIntf> pageParams = new Query<>(params);
        PageUtils pageUtils = cnbmIntfService.queryPage(pageParams);
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
        return Result.success(cnbmIntfService.selectById(id));
    }

    /**
     * 保存或者修改
     */
    @PostMapping("/saveOrUpdate")
    @Transactional
    public Result saveOrUpdate(@ModelAttribute CnbmIntf cnbmIntf) {
        try {
            //编码是否存在
            EntityWrapper<CnbmIntf> wrapper = new EntityWrapper<>();
            wrapper.eq("code", cnbmIntf.getCode());
            if (StringUtils.isBlank(cnbmIntf.getId()) && cnbmIntfService.selectCount(wrapper) > 0) {
                return Result.failed("接口编码" + cnbmIntf.getCode() + "已经存在");
            }
            cnbmIntfService.insertOrUpdate(cnbmIntf);
            return Result.success("保存成功");
        } catch (Exception e) {
            log.error("保存接口信息失败，参数：{}，错误信息：{}", cnbmIntf, e);
            return Result.failed("保存接口信息失败");
        }
    }


    /**
     * 删除,支持批量删除
     */
    @PostMapping("/delete")
    @Transactional
    public Result delete(@RequestParam(value = "ids[]") String[] ids) {
        boolean result = cnbmIntfService.deleteBatchIds(Arrays.asList(ids));
        if (result) {
            return Result.success("删除接口信息成功");
        } else {
            return Result.failed("删除接口信息失败");
        }
    }
}
