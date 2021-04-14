package com.cnbm.intf.core.utils;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.thymeleaf.util.MapUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class XmlUtil {
    public static final String NEWLINE_CHARACTER = "\r\n";

    /**
     * 将object转化为xml
     * @param obj 对象
     * @return xml String类型
     */
    public static String objectToXml(Object obj) {
        String result = null;
        if (obj != null) {
            StringWriter writer = new StringWriter();
            JAXBContext context;
            try {
                context = JAXBContext.newInstance(obj.getClass());
                Marshaller marshaller = context.createMarshaller();
                //marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                marshaller.marshal(obj, writer);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            result = writer.toString();
        }
        return result;
    }

    /**
     * xml转object
     * @param <T>
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToObject(String fromXml, Class<T> clazz) throws JAXBException {
        StringReader reader = new StringReader(fromXml);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T)unmarshaller.unmarshal(reader);
    }

    /**
     * 将Xml转化为Map<String,Object>
     * @param xmlStr xml文本
     * @return Map<String,Object>
     */
    public static Map<String, Object> xmlToMap(String xmlStr) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
            Element rootElement = doc.getRootElement();
            return XmlUtil.elementsResolve(rootElement);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Map转换为XML
     * @param mapObj Map对象
     * @return XML格式
     */
    @SuppressWarnings("unchecked")
    public static String mapToXml(Map<String, Object> mapObj) {
        if (MapUtils.isEmpty(mapObj)) {
            return null;
        }
        //使用DOM4J方式封装，可以进行格式化
        //创建主节点
        Document doc = DocumentHelper.createDocument();
        for (String key : mapObj.keySet()) {
            Element root = doc.addElement(key);
            XmlUtil.mapToXmlChildMap((Map<String, Object>)mapObj.get(key), root);
        }
        //设置文件编码
        OutputFormat xmlFormat = new OutputFormat();
        xmlFormat.setEncoding("UTF-8");
        //设置换行
        xmlFormat.setNewlines(true);
        //生成缩进
        xmlFormat.setIndent(true);
        //使用4个空格进行缩进，可以兼容文本编辑器
        xmlFormat.setIndent("    ");
        //创建字符串缓冲区
        StringWriter stringWriter = new StringWriter();
        //创建写文件方法
        XMLWriter xmlWriter = new XMLWriter(stringWriter, xmlFormat);
        //写入文件
        try {
            xmlWriter.write(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭
            try {
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringWriter.toString();
    }

    @SuppressWarnings("unchecked")
    public static void mapToXmlChildMap(Map<String, Object> mapObj, Element element) {
        for (String key : mapObj.keySet()) {
            Object value = mapObj.get(key);
            if (null == value) {
                //没有输入value直接按""处理
                value = "";
                element.addElement(key).addText((String)value);
            } else if (value instanceof List) {
                List<Map<String, Object>> list = (List<Map<String, Object>>)value;
                Element elementList = element.addElement(key);
                for (Map<String, Object> childMap : list) {
                    XmlUtil.mapToXmlChildMap(childMap, elementList);
                }
            } else if (value instanceof Map) {
                Element elementMap = element.addElement(key);
                XmlUtil.mapToXmlChildMap((Map<String, Object>)value, elementMap);
            } else {
                element.addElement(key).addText(XmlUtil.valueFormat(value));
            }
        }
    }

    /**
     * value转对应的格式
     * @param value Object
     * @return 对应值
     */
    public static String valueFormat(Object value) {
        if (value instanceof Integer) {
            return Integer.toString((Integer)value);
        } else if (value instanceof Double) {
            return Double.toString((Double)value);
        } else if (value instanceof Short) {
            return Short.toString((Short)value);
        } else if (value instanceof Date) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(value);
        } else if (value instanceof Long) {
            return Long.toString((Long)value);
        } else {
            return (String)value;
        }
    }

    /***
     * 递归调用节点中的值,然后以map返回结果
     * @param elementInput 节点
     * @return Map<String,Object>
     */
    @SuppressWarnings("unchecked")
    static Map<String, Object> elementsResolve(Element elementInput) {
        Map<String, Object> mapObject = new HashMap<String, Object>();
        //获得当前节点的子节点
        List<Element> elements = elementInput.elements();
        if (elements.size() == 0) {
            //没有子节点说明当前节点是叶子节点，直接取值即可
            mapObject.put(elementInput.getName(), elementInput.getText());
        } else if (elements.size() == 1) {
            //只有一个子节点说明不用考虑list的情况，直接继续递归即可
            mapObject.put(elementInput.getName(), XmlUtil.elementsResolve(elements.get(0)));
        } else {
            //多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的构造一个map用来去重
            Map<String, Element> elementListTemp = new HashMap<String, Element>();
            for (Element elementTemp : elements) {
                elementListTemp.put(elementTemp.getName(), elementTemp);
            }
            Set<String> keySet = elementListTemp.keySet();
            for (String elementName : keySet) {
                Namespace namespace = elementListTemp.get(elementName).getNamespace();
                List<Element> elementList = elementInput.elements(new QName(elementName, namespace));
                //如果同名的数目大于1则表示要构建list
                if (elementList.size() > 1) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (Element element : elementList) {
                        list.add(XmlUtil.elementsResolve(element));
                    }
                    mapObject.put(elementName, list);
                } else {
                    //同名的数量不大于1则直接递归去,若节点下面没有节点后，直接赋值
                    if (elementList.get(0).elements().size() == 0) {
                        mapObject.putAll(XmlUtil.elementsResolve(elementList.get(0)));
                    } else {
                        mapObject.put(elementName, XmlUtil.elementsResolve(elementList.get(0)));
                    }
                }
            }
        }
        //处理主节点
        if (elementInput.isRootElement()) {
            Map<String, Object> rootMap = new HashMap<String, Object>();
            rootMap.put(elementInput.getName(), mapObject);
            return rootMap;
        }
        return mapObject;
    }
}
