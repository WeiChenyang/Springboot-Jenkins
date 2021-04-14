package com.cnbm.intf.core.webservice;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 *
 * @create 2019031113:29
 */
@Slf4j
public abstract class CommonWebServiceClient<T> extends CommonWebservice {
    private String exceptionMsg;

    protected abstract CommonInterfaceResult processResult(Object t);

    /**
     * 参数校验
     *
     * @return
     */
    private boolean validateParam() {
        CommonParam params = packageParams();
        log.info("-##validateParam:{}", params);
        if (params == null) {
            return false;
        }
        // 校验参数
        log.info("接口参数xml:{}", params.getParams());
        return true;
    }

    public boolean processInfo() {
        if (!validateParam()) {
            return false;
        }
        CommonParam params = null;
        try {
            params = packageParams();
            Class<T> action = params.getCls();//获取类
            Constructor constructor = action.getConstructor(String.class);
            Object newInstance = constructor.newInstance(params.getUrl());
            Method declaredMethod = action.getDeclaredMethod(params.getMethodName(), getMethodParamTypes(newInstance, params.getMethodName()));
            //Class<?> returnType = declaredMethod.getReturnType();
            Object invoke = declaredMethod.invoke(newInstance, params.getParams());
            cnbmIntfLog.setInputReturnVal(JSON.toJSONString(invoke));
            CommonInterfaceResult interfaceResult = processResult(invoke);
            //如果对方直接返回null就报异常，没有按规范写
            if (interfaceResult == null) {
                this.recordIntfcLog(false, "未按规范要求开发", "processResult返回null，请按照CommonInterfaceResult对象填写信息", params);
                return false;
            }
            //返回请求返回值
            cnbmIntfLog.setInputReturnVal(JSON.toJSONString(interfaceResult.getOut()));
            this.recordIntfcLog(interfaceResult.isSuccess(), "", "", params);
            return true;
        } catch (Exception e) {
            if (retryApp(params, e)) return processInfo();
        }
        return false;
    }

    //请求异常信息
    public String getExceptionMsg() {
        return exceptionMsg;
    }

    /**
     * 根据方法名称取得反射方法的参数类型(没有考虑同名重载方法使用时注意)
     *
     * @param classInstance 类实例
     * @param methodName    方法名
     * @return
     * @throws ClassNotFoundException
     */
    private Class[] getMethodParamTypes(Object classInstance,
                                        String methodName) throws ClassNotFoundException {
        Class[] paramTypes = null;
        Method[] methods = classInstance.getClass().getMethods();//全部方法
        for (int i = 0; i < methods.length; i++) {
            if (methodName.equals(methods[i].getName())) {//和传入方法名匹配
                Class[] params = methods[i].getParameterTypes();
                paramTypes = new Class[params.length];
                for (int j = 0; j < params.length; j++) {
                    paramTypes[j] = Class.forName(params[j].getName());
                }
                break;
            }
        }
        return paramTypes;
    }
}
