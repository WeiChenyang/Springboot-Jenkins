package com.cnbm.intf.core.common;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cnbm.intf.core.common.utils.Query;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @create 2019022915:15
 */
public interface BaseCnbmMapper<T> extends BaseMapper<T> {

    List<T> queryPage(Page<T> pageParams, @Param("params") Query<T> params);
}
