/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.integration;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gw.recharge.dal.model.OrderDO;
import com.gw.recharge.dal.model.VendorDO;
import com.gw.recharge.rt.integration.dto.RechargeResponseDto;
import com.gw.recharge.rt.service.OrderStatus;
import com.gw.recharge.rt.util.BizResourcesUtil;
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.steel.steel.util.httpclient.HttpClient;
import com.gw.steel.steel.util.security.AES;
import com.gw.steel.steel.util.security.MD5;
import com.gw.steel.steel.util.security.StringUtil;

/**
 * 
 * @author log.yin
 * @version $Id: NskRechargeService.java, v 0.1 2015年3月30日 下午2:50:52 log.yin Exp $
 */
public class NskRechargeService extends AbstractRefRechargeService {
    private static final Logger logger = LoggerFactory.getLogger(NskRechargeService.class);

    @Override
    protected String callRemoteCharge(OrderDO order, VendorDO vendorDO) {
        String url = BizResourcesUtil.NSK_RECHARGE_URL;
        Map<String, String> requestParamMap = buildReChargeRequestParam(order);
        String queryString = getQueryString(requestParamMap);
        String requestStr = url + queryString;
        String respStr = "";
        try {
            logger.info("NSK recharge request: {}",
                StringUtil.ignoreMobile(requestStr, "mobiles=", 11));
            respStr = HttpClient.get(requestStr);
            logger.info("NSK recharge response: {}", respStr);
        } catch (Exception e) {
            logger.error("", e);
        }
        return respStr;
    }

    @Override
    protected String callRemoteQuery(OrderDO order, VendorDO vendorDO) {
        return null;
    }

    @Override
    protected RechargeResponseDto handleRechargeResponse(String responseStr) {
        RechargeResponseDto dto = null;
        String[] responseStrArr = responseStr.split(",");
        if (responseStrArr.length > 0) {
            dto = new RechargeResponseDto();
            String orderNo = responseStrArr[0];
            String result = responseStrArr[1];
            String intercept = "";
            if (responseStrArr.length == 5) {
                intercept = responseStrArr[4];
                dto.setRefRespMsg("");
                logger.info("result={}, intercept={}", result, intercept);
            }
            dto.setOrderId(orderNo);
            dto.setRefRespCode(result);

            //success
            if ("0".equals(result)) {
                dto.setCode(CodeConstants.SUCCESS);
                dto.setOrderStatus(OrderStatus.SUCCESS);
            } else {
                dto.setCode(CodeConstants.UNKNOWN_ERROR);
                dto.setOrderStatus(OrderStatus.FAILED);
            }
        }
        return dto;
    }

    @Override
    protected RechargeResponseDto handleQueryResponse(String responseStr) {
        return null;
    }

    private Map<String, String> buildReChargeRequestParam(OrderDO order) {
        Map<String, String> requestParam = new TreeMap<String, String>();
        String mobileMingWen = "";
        try {
            mobileMingWen = AES.decrypt(BizResourcesUtil.SECURITY_AES_KEY, order.getMobileAes());
        } catch (Exception e) {
            logger.error("", e);
        }

        requestParam.put("user", BizResourcesUtil.NSK_RECHARGE_USER);
        String passwd = MD5.md5Hex(BizResourcesUtil.NSK_RECHARGE_USER
                                   + BizResourcesUtil.NSK_RECHARGE_PASSWD);
        requestParam.put("passwd", passwd);
        requestParam.put("orderID", order.getOrderId());
        requestParam.put("mobiles", mobileMingWen);
        requestParam.put("money", order.getAmount() + "");
        requestParam.put("dtype", "0");

        return requestParam;
    }

}
