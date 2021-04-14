package com.cnbm.intf.core.common.utils;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @create 2019022913:34
 **/
@Data
public class Query<T> extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    /**
     * mybatis-plus分页参数
     */
    private Page<T> page;
    /**
     * 当前页码
     */
    private int currPage = 1;
    /**
     * 每页条数
     */
    private int limit = 10;

    public Query(Map<String, Object> params) {
        this.putAll(params);
        if (params.get("limit") != null) {
            limit = Integer.parseInt((String) params.get("limit"));
        }
        //分页参数
        if (params.get("offset") != null) {
            currPage = Integer.parseInt((String) params.get("offset"));

        }
        currPage = (currPage / limit) + 1;
        this.put("currPage", currPage);
        this.put("limit", limit);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject((String) params.get("sort"));
        String order = SQLFilter.sqlInject((String) params.get("order"));
        this.put("sort", sidx);
        this.put("order", order);

        //mybatis-plus分页
        this.page = new Page<>(currPage, limit);

        //排序
        if (StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)) {
            this.page.setOrderByField(sidx);
            this.page.setAsc("ASC".equalsIgnoreCase(order));
        }

    }
}
