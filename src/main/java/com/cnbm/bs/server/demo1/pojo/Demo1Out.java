package com.cnbm.bs.server.demo1.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @create 2019031110:29
 */
@Data
@XmlRootElement(name="OUT")
@XmlAccessorType(XmlAccessType.FIELD)
public class Demo1Out {
    @XmlElement
    @JSONField
    private String isFlag;
}
