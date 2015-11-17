/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gw.recharge.rt.service.OrderService;

/**
 * 客户端充值结果查询
 * 订单状态返回“成功、失败、充值中
 * 
 * @author log.yin
 * @version $Id: RechargeQueryController.java, v 0.1 2015年2月4日 下午3:16:42 log.yin Exp $
 */
@Controller
public class RechargeQueryController {
    private static final Logger logger = LoggerFactory.getLogger(RechargeQueryController.class);

    @Autowired
    private OrderService        orderService;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public String receiveNotice() {
        logger.info("");
        return "Waiting...";
    }
}
