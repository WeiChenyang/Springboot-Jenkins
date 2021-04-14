package com.cnbm.intf.core.common.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbm.intf.core.common.BaseCnbmMapper;
import com.cnbm.intf.core.common.BaseModel;
import com.cnbm.intf.core.common.service.IBaseService;
import com.cnbm.intf.core.common.utils.PageUtils;
import com.cnbm.intf.core.common.utils.Query;

import java.util.List;

/**
 * 数据事务处理公共方法
 *
 *
 * @create 2019022515:33
 */
public class BaseServiceImpl<M extends BaseCnbmMapper<T>, T extends BaseModel<T>> extends ServiceImpl<M, T> implements IBaseService<T> {
    /**
     * 查询数据时自动处理ID，同时方便扩展日志
     *
     * @param entity
     * @return
     */
    @Override
    public boolean insertExt(T entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            this.baseMapper.insert(entity);
        } else {
            entity.preUpdate();
            this.baseMapper.update(entity, null);
        }
        return false;
    }

    @Override
    public PageUtils queryPage(Query<T> params) {
        Page<T> pageParams = params.getPage();
        List<T> intfs = baseMapper.queryPage(pageParams, params);
        pageParams.setRecords(intfs);
        return new PageUtils(pageParams);
    }
}
