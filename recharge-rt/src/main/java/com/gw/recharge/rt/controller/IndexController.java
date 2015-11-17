/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 测试服务时候正常运行
 * @author log.yin
 * @version $Id: TestController.java, v 0.1 2015年2月9日 上午10:07:30 log.yin Exp $
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    protected String index() {
        return "Service is running";
    }

}
