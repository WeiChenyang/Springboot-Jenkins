package com.cnbm.intf.core.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cnbm.intf.core.common.BaseModel;
import lombok.Data;

/**
 * 接口表
 * <p>
 * 主要记录接口的生产者和消费者信息
 *
 *
 * @create 2019022913:34
 */
@Data
@TableName("cnbm_intf")
public class CnbmIntf extends BaseModel<CnbmIntf> {

    private String code;               //接口编码
    private String name;               //接口名称
    private String inputName;         //生产者系统名称
    private String inputUrl;          //生产者系统地址
    private String inputParamFormat; //生产者系统传入参数格式
    private String outputName;        //消费者系统名称
    private String outputFormat;      //消费者系统传入参数格式
    private String inputClassName;   //生产者系统类名
    private String inputMethodName;  //生产者系统方法名
    private String outputClassName;  //消费者系统类名
    private String outputMethodName; //消费者系统方法名
    private String isRetry;         //是否重试
    private Integer maxRetryCount;         //重试次数
    private Integer timeout;         //超时时间
    private String status;             //状态0启用，1禁用
    private String remarks;
    private String ipAddress;

}
