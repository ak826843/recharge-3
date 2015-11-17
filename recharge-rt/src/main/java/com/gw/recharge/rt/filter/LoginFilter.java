/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * @author log.yin
 * @version $Id: LoginFilter.java, v 0.1 2015年4月22日 下午7:32:43 log.yin Exp $
 */
public class LoginFilter implements Filter {

/*    private static Map<String, String> userMap  = new HashMap<String, String>();
    private final String               USER_MAP = "userMap";
*/
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /*try {
            String userAndPassword = BizResourcesUtil.WEBRECHARGE_USER_AND_PASSWORD;

            String[] users = userAndPassword.split("\\|");
            for (int i = 0; i < users.length; i++) {
                String[] user = users[i].split(",");
                userMap.put(user[0], user[1]);
            }
        } catch (Exception e) {
            logger.error("", e);
        }*/
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        return;
        /*if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            HttpSession session = request.getSession();
            String url = request.getRequestURI();

            Object users = session.getAttribute(USER_MAP);
            if (users == null) {
                session.setAttribute(USER_MAP, userMap);
                session.setMaxInactiveInterval(3 * 60);
            }

            //需要登录的url
            if (url.indexOf("webrecharge") != -1) {
                Object sessionUser = session.getAttribute("user");
                Object sessionPassword = session.getAttribute("password");

                //未登录
                if (sessionUser == null || sessionUser.toString().trim().length() == 0) {
                    response.sendRedirect("login");
                    return;
                }

                if (sessionPassword == null || sessionPassword.toString().trim().length() == 0) {
                    response.sendRedirect("login");
                    return;
                }

                if (!userMap.containsKey(sessionUser)
                    || !userMap.get(sessionUser).equals(sessionPassword)) {
                    request.setAttribute("user.error", "登录信息错误");
                    response.sendRedirect("login");
                    return;
                }
            }

            chain.doFilter(request, response);
            return;
        }*/

    }

    @Override
    public void destroy() {
    }

/*    public static Map<String, String> getUserMap() {
        return userMap;
    }*/

}
