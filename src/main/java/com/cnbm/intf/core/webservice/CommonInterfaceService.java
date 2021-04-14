package com.cnbm.intf.core.webservice;

import com.cnbm.intf.core.utils.ExtStringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CommonInterfaceService<I, O> {

    protected class CommonInterfaceResult {
        private boolean success;
        private O out;

        public CommonInterfaceResult(boolean success, O out) {
            this.success = success;
            this.out = out;
        }

        public boolean isSuccess() {
            return success;
        }

        public O getOut() {
            return out;
        }
    }

    public O process(I in) {
        O out = null;
        String msg = null;
        try {
            O vldResult = validateInput(in);
            if (vldResult != null) {
                msg = "校验接口参数出错";
                log.info(msg);
                out = vldResult;
                this.intfcLog(in, out, false, msg, null);
            } else {
                CommonInterfaceResult result = this.execute(in);
                out = result.getOut();
                boolean success = result.isSuccess();
                msg = success ? "接口调用成功" : "接口调用出错";
                this.intfcLog(in, out, success, msg, null);
            }
        } catch (Exception e) {
            msg = "调用接口发生异常";
            log.error(msg, e);
            out = handleError(in, e);
            this.intfcLog(in, out, false, msg, ExtStringUtils.excpStackTraceStr(e));
        }
        return out;
    }

    /**
     * 校验输入参数
     * @param in 输入参数
     * @return 校验通过返回null，校验失败返回封装好的校验出错结果对象
     */
    protected abstract O validateInput(I in);

    /**
     * 接口业务方法
     * @param in
     * @return 执行结果包装对象
     */
    protected abstract CommonInterfaceResult execute(I in) throws Exception;

    /**
     * 处理异常错误
     * @param in 输入参数
     * @param e 异常对象
     * @return 封装好的异常结果对象
     */
    protected abstract O handleError(I in, Exception e);

    private void intfcLog(I in, O out, boolean success, String msg, String detail) {
        // 记录外部接口日志

    }
}
