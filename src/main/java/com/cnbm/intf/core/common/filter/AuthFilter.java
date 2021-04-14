package com.cnbm.intf.core.common.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 *
 * @create 2019031210:10
 */
@Slf4j
public class AuthFilter implements Filter {

    //servlet容器初始化时
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //servlet容器存在时
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    //servlet容器销毁时
    @Override
    public void destroy() {

    }
}
