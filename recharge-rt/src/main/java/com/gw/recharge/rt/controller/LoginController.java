/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author log.yin
 * @version $Id: LoginController.java, v 0.1 2015年4月22日 下午7:43:36 log.yin Exp $
 */
@Controller
@RequestMapping("app-no-used")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login")
    public Object login(HttpServletRequest request, HttpServletResponse response, ModelAndView mov) {
        logger.info("show login page");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");

        Object loginTip = request.getAttribute("user.error");
        mv.addObject("message", "");
        if (loginTip != null) {
            mv.addObject("message", loginTip);
        }
        return mv;
    }

    @RequestMapping(value = "/loginon", method = RequestMethod.POST)
    public Object loginon(String user, String password, HttpServletRequest request,
                          HttpServletResponse response, ModelAndView mov) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("webrecharge");
        logger.info("show webrecharge page");
        
        Object users = request.getSession().getAttribute("userMap");
        if (users == null) {
            logger.info("Not found user in session");
            request.setAttribute("user.error", "user不存在");
            mv.setViewName("login");
            return mv;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> userMap = (Map<String, String>) users;
        if (!userMap.containsKey(user) || !userMap.get(user).equals((password))) {
            logger.info("Not found user in config");
            request.setAttribute("user.error", "user不存在");
            mv.setViewName("login");
            mv.addObject("message", "用户名或密码错误");
            return mv;
        }

        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("password", password);
        return mv;
    }
}
