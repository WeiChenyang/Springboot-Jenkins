package com.cnbm.intf.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExtStringUtils {
    private static final char ESC = '\\';
    private static final char TOKEN_START = '{';
    private static final char TOKEN_END = '}';
    private static final byte MODE_TXT = 0;
    private static final byte MODE_TOKEN = 1;
    private static final byte MODE_ESC = 2;

    /**
     * 将字符串转义以适用于直接以字面量拼接到Javascript字符串中（以<code>"</code>作为Javascript使用的引号字符）<br>
     * 例如：<br>
     * <code>{account:"domain\Jack"}</code><br>
     * 以上字符串在转义后将会得到：<br>
     * <code>{account:\"domain\\Jack\"}</code>
     * @param str 要转义的字符串
     * @return 转义后的字符串
     */
    public static String escapeForJsStr(String str) {
        return escapeForJsStr(str, "\"");
    }

    /**
     * 将字符串转义以适用于直接以字面量拼接到Javascript字符串中（以指定的<code>quot</code>字符作为Javascript使用的引号字符）<br>
     * 例如：<br>
     * <code>{account:"domain\Jack"}</code><br>
     * 以上字符串在使用<code>"</code>作为引号符转义后将会得到：<br>
     * <code>{account:\"domain\\Jack\"}</code>
     * @param str 要转义的字符串
     * @param quot Javascript使用的引号字符
     * @return 转义后的字符串
     */
    public static String escapeForJsStr(String str, String quot) {
        String result = null;
        if (str != null) {
            result = str.replace("\\", "\\\\");
            result = result.replace(quot, "\\" + quot);
            result = result.replace("\r\n", "\\n");
            result = result.replace("\n", "\\n");
            result = result.replace("\r", "\\r");
            result = result.replace("/", "\\/");
        }
        return result;
    }

    /**
     * 将字符串转义以适用于直接输出在html页面中<br>
     * 例如：<br>
     * <code>String flag = num1 &lt; 100 &amp;&amp; num2 &gt;= 0 ? "ok" : "err: &nbsp; &nbsp;number out of range.";</code><br>
     * 转义后变为：<br>
     * <code>String flag = num1 &amp;lt; 100 &amp;amp;&amp;amp; num2 &amp;gt;= 0 ? "ok" : "err: &amp;nbsp; &amp;nbsp;number out of range.";</code>
     * @param str 要转义的字符串
     * @return 转义后的字符串
     */
    public static String escapeHtml(String str) {
        String result = str;
        if (result != null) {
            result = result.replace("&", "&amp;");
            result = result.replace("<", "&lt;");
            result = result.replace(">", "&gt;");
            result = result.replace("\"", "&quot;");
            result = result.replace("'", "&apos;");
            result = result.replace("\r\n", "<br/>");
            result = result.replace("\n", "<br/>");
            result = result.replace("\r", "<br/>");
            result = result.replaceAll("\\s{2}", " &nbsp;");
        }
        return result;
    }

    /**
     * 提取异常对象中的stack trace内容为字符串，结果与printStackTrace()方法输出的异常信息内容相同，便于传递到其它地方以展示异常详情
     * @param t 要提取stack trace的异常对象
     * @return 提取的所有stack trace内容字符串
     */
    public static String excpStackTraceStr(final Throwable t) {
        StringWriter buff = new StringWriter(1024);
        PrintWriter writer = new PrintWriter(buff);
        t.printStackTrace(writer);
        writer.flush();
        writer.close();
        return buff.toString();
    }

    /**
     * 将异常消息拼接在指定字符串之后，并以括号包括起来<br>
     * 如：保存失败！ (java.math.BigDecimal can not be cast into java.util.Date)
     * @param msg 指定的字符串
     * @param t 异常对象
     * @return 拼接后的字符串，若异常消息为空则返回原字符串
     */
    public static String appendExcpMsg(String msg, Throwable t) {
        String result = msg;
        if (t != null) {
            StringBuilder buff = new StringBuilder();
            if (msg != null) {
                buff.append(msg);
            }
            String err = t.getMessage();
            if (StringUtils.isNotEmpty(err)) {
                buff.append(" (");
                buff.append(err);
                buff.append(")");
            }
            result = buff.toString();
        }
        return result;
    }

    public static String format(String template, Object... params) {
        String result = template;
        if (result != null) {
            int len = result.length();
            int i = 0;
            byte mode = MODE_TXT;
            StringBuilder buff = new StringBuilder();
            StringBuilder tokenBuff = new StringBuilder();
            for (; i < len; i++) {
                char c = result.charAt(i);
                switch (mode) {
                case MODE_TXT:
                    switch (c) {
                    case TOKEN_START:
                        mode = MODE_TOKEN;
                        break;
                    case ESC:
                        mode = MODE_ESC;
                        break;
                    default:
                        buff.append(c);
                        break;
                    }
                    break;
                case MODE_TOKEN:
                    switch (c) {
                    case TOKEN_END:
                        String token = tokenBuff.toString();
                        if (token != null && token.matches("^[0-9]{1,9}$")) {
                            int tokenIdx = Integer.parseInt(token);
                            if (tokenIdx < params.length) {
                                Object param = params[tokenIdx];
                                if (param != null) {
                                    buff.append(param.toString());
                                }
                            }
                        } else {
                            buff.append(TOKEN_START);
                            buff.append(token == null ? "" : token);
                            buff.append(TOKEN_END);
                        }
                        tokenBuff = new StringBuilder();
                        mode = MODE_TXT;
                        break;
                    default:
                        tokenBuff.append(c);
                        break;
                    }
                    break;
                case MODE_ESC:
                    buff.append(c);
                    mode = MODE_TXT;
                    break;
                default:
                    break;
                }
            }
            result = buff.toString();
        }
        return result;
    }

    public ExtStringUtils() {
        // 禁止实例化工具类
    }
}
