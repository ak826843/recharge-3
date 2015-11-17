/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller.vendor;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gw.recharge.rt.integration.dto.yq.YQConstants;
import com.gw.recharge.rt.service.OrderService;
import com.gw.recharge.rt.service.OrderStatus;
import com.gw.recharge.rt.util.BizResourcesUtil;
import com.gw.steel.steel.util.common.Md5ParamsGenerator;

/**
 * 
 * @author log.yin
 * @version $Id: YQNotifyController.java, v 0.1 2015年2月4日 下午3:16:42 log.yin Exp $
 */
@Controller
public class YQNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(YQNotifyController.class);

    @Autowired
    private OrderService        orderService;

    @RequestMapping(value = "/notify", method = RequestMethod.GET, produces = { "application/xml;charset=UTF-8" })
    @ResponseBody
    public String receiveNotice(@RequestParam("MerchantId") String MerchantId,
                                @RequestParam("MerchantOrderId") String MerchantOrderId,
                                @RequestParam("MerchantOrderStatus") String MerchantOrderStatus,
                                @RequestParam("YqOrderNo") String YqOrderNo,
                                @RequestParam("SuccessTime") String SuccessTime,
                                @RequestParam("sign") String sign) {

        Map<String, String> map = new TreeMap<String, String>();
        map.put(YQConstants.MerchantId, MerchantId);
        map.put(YQConstants.MerchantOrderId, MerchantOrderId);
        map.put(YQConstants.MerchantOrderStatus, MerchantOrderStatus);
        if (OrderStatus.SUCCESS.equals(MerchantOrderStatus)) {
            map.put(YQConstants.SuccessTime, SuccessTime);
        }
        map.put(YQConstants.YqOrderNo, YqOrderNo);
        logger.info("Vendor notice params: {}", map);

        String signString = Md5ParamsGenerator.generateHttpGetParams(map)
                            + BizResourcesUtil.YQ_MD5_KEY;
        String signValue = DigestUtils.md5Hex(signString).toLowerCase();

        logger.info("signString: {}, signValue: {}", signString, signValue);

        if (!signValue.equals(sign)) {
            return "<?xml version=\"1.0\" encoding=\"UTF8\"?><response><Status>sign验证不通过</Status></response>";
        }

        //通知的订单状态为SUCCESS,FAILED是才更新
        if (OrderStatus.SUCCESS.equals(MerchantOrderStatus)
            || OrderStatus.FAILED.equals(MerchantOrderStatus)) {
            orderService.updateOrder(MerchantOrderId, YqOrderNo, MerchantOrderStatus, SuccessTime);
        }

        return "<?xml version=\"1.0\" encoding=\"UTF8\"?><response><Status>T</Status></response>";
    }
}
