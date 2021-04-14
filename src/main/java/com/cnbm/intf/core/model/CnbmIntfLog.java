package com.cnbm.intf.core.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cnbm.intf.core.common.BaseModel;
import lombok.Data;

/**
 * 接口日志表
 *
 *
 * @create 2019022913:34
 */
@Data
@TableName("cnbm_intf_log")
public class CnbmIntfLog extends BaseModel<CnbmIntfLog> {

    private String intfId; //接口管理表
    private String intfName; //接口名称
    private String status; //状态0成功1失败
    private String inputStatus; //生产者调用状态0成功1失败
    private String inputParams; //生产者调用参数
    private String inputReturnVal; //生产者响应结果
    private String outputStatus; //消费者调用状态0成功1失败
    private String outputParams; //消费者调用参数
    private String failedReason; //失败原因
    private String ipAddress; //ip地址
    private String url; //访问地址
    private String methodExt; //调用方法
    private String responseTimeExt; //响应时间
    private Integer retryCount;//记录重试次数
}
