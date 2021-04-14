package com.cnbm.intf.core.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.cnbm.intf.core.common.utils.PageUtils;
import com.cnbm.intf.core.common.utils.Query;

/**
 * 服务公共处理
 *
 * @create 2019022515:25
 */
public interface IBaseService<T> extends IService<T> {
    boolean insertExt(T entity);

    PageUtils queryPage(Query<T> pageParams);
}
