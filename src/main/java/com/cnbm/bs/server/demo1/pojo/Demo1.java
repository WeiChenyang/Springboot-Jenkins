package com.cnbm.bs.server.demo1.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 对象转xml
 *
 * @create 201903119:44
 */
@Data
@XmlRootElement(name="IN")
@XmlAccessorType(XmlAccessType.FIELD)
public class Demo1 {
    @XmlElement
    @JSONField
    private String name;
    @XmlElement
    @JSONField
    private String sex;
    @XmlElement
    @JSONField
    private String age;
    @XmlElement(name = "PI_LANGUAGE")//字段信息不一致是
    @JSONField(name = "PI_LANGUAGE")
    private String language;
}
