package com.irs.interceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录验证
 */
public class LoginVerifyFilter implements Filter {

    private  List<String> excludeList ;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String loginUrl = filterConfig.getInitParameter("loginUrl");
        String[] excludes = filterConfig.getInitParameter("excludes").split("\n");
        excludeList = Arrays.asList(excludes).stream().map(e -> e.replace(",", "")).collect(Collectors.toList());
        excludeList.add(loginUrl);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url = request.getRequestURI();
        if(checkUrl(url)){//登录放过 静态资源放过
            chain.doFilter(request, response);
            return;
        }
        if(request.getSession().getAttribute("admin")== null){//其余请求验证sesion信息
            String contextPath = getContextPath(request);
            response.sendRedirect(contextPath+ "/login.jsp");
            return;
        }
        chain.doFilter(request, response);
        return;
    }

    private boolean checkUrl(String url) {
        //excludeList 内置 登录url 和 过滤资源url
        return excludeList.stream().anyMatch(e -> url.contains(e.trim()));
    }

    protected String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        if ((contextPath == null) || (contextPath.equals("/"))) {
            return "";
        }
        return contextPath;
    }

    @Override
    public void destroy() {

    }
}
