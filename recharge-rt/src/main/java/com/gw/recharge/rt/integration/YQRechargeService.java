/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.integration;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gw.recharge.dal.model.OrderDO;
import com.gw.recharge.dal.model.VendorDO;
import com.gw.recharge.rt.integration.dto.RechargeResponseDto;
import com.gw.recharge.rt.integration.dto.yq.YQConstants;
import com.gw.recharge.rt.integration.dto.yq.YQReChargeResponseDto;
import com.gw.recharge.rt.service.OrderStatus;
import com.gw.recharge.rt.util.BizResourcesUtil;
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.steel.spring.rediscache.RedisCacheEngine;
import com.gw.steel.steel.util.common.Md5ParamsGenerator;
import com.gw.steel.steel.util.common.XmlUtils;
import com.gw.steel.steel.util.httpclient.HttpClient;
import com.gw.steel.steel.util.security.AES;
import com.gw.steel.steel.util.security.StringUtil;

public class YQRechargeService extends AbstractRefRechargeService {

    private static final Logger logger = LoggerFactory.getLogger(YQRechargeService.class);

    @Autowired
    private RedisCacheEngine    redisCacheEngine;

    @Override
    protected String callRemoteCharge(OrderDO order, VendorDO vendorDO) {
        String url = BizResourcesUtil.YQ_RECHARGE_URL;
        Map<String, String> requestParamMap = buildReChargeRequestParam(order, vendorDO);
        String queryString = getQueryString(requestParamMap);
        String requestStr = url + queryString;
        String respStr = "";
        try {
            logger.info("YQ recharge request: {}",
                StringUtil.ignoreMobile(requestStr, "Account=", 11));
            respStr = HttpClient.get(requestStr);
            logger.info("YQ recharge response: {}", respStr);
        } catch (Exception e) {
            logger.error("", e);
        }
        return respStr;
    }

    @Override
    protected String callRemoteQuery(OrderDO order, VendorDO vendorDO) {
        Map<String, String> requestParamMap = buildReOrderQueryRequestParam(order, vendorDO);
        String queryString = getQueryString(requestParamMap);
        String requestStr = BizResourcesUtil.YQ_QUERY_URL + queryString;
        String respStr = "";
        try {
            logger
                .info("YQ query request: {}", StringUtil.ignoreMobile(requestStr, "Account=", 11));
            respStr = HttpClient.get(requestStr);
            logger.info("YQ query response: {}", respStr);
        } catch (IOException e) {
            logger.error("", e);
        }
        return respStr;
    }

    /**
     * 当意桥返回错误码为205（不支持的手机号）时， 认为是不合法的请求， 返回错误状态码为10001。 其他情况
     * 都视为接单成功，具体充值结果由订单查询调度任务来更新。
     */
    @Override
    protected RechargeResponseDto handleRechargeResponse(String responseStr) {
        YQReChargeResponseDto responseDto = XmlUtils.xml2ObjectByJAXB(responseStr,
            YQReChargeResponseDto.class);
        RechargeResponseDto rechargeRespDto = new RechargeResponseDto();
        rechargeRespDto.setRefRespCode(responseDto.getFailCode());
        rechargeRespDto.setRefRespMsg(responseDto.getFailDesc());
        rechargeRespDto.setOrderId(responseDto.getOrderId());

        //充值接口返回的状态为UNDERWAY或 REQUEST_FAILED，最终结果异步通知或查询
        rechargeRespDto.setCode(CodeConstants.SUCCESS);
        rechargeRespDto.setOrderStatus(responseDto.getOrderStatus());

        return rechargeRespDto;
    }

    @Override
    protected RechargeResponseDto handleQueryResponse(String responseStr) {
        YQReChargeResponseDto responseDto = XmlUtils.xml2ObjectByJAXB(responseStr,
            YQReChargeResponseDto.class);
        RechargeResponseDto rechargeRespDto = new RechargeResponseDto();
        rechargeRespDto.setRefRespCode(responseDto.getFailCode());
        rechargeRespDto.setRefRespMsg(responseDto.getFailDesc());
        rechargeRespDto.setOrderId(responseDto.getOrderId());
        String orderStatus = responseDto.getOrderStatus();
        // 需要根据第三方订单状态转成系统定义的订单状态
        rechargeRespDto.setOrderStatus(orderStatus);
        if (OrderStatus.SUCCESS.equals(orderStatus)) {
            //TODO 发通知给前端APP 
        }
        return rechargeRespDto;
    }

    /**
     * 根据订单构建充值的请求
     * 
     * @param order
     * @return
     */
    private Map<String, String> buildReChargeRequestParam(OrderDO order, VendorDO vendorDO) {
        Map<String, String> requestParam = new TreeMap<String, String>();
        requestParam.put(YQConstants.Code, vendorDO.getVendorCode());
        String orderId = order.getOrderId();
        requestParam.put(YQConstants.OrderID, orderId);
        String mobileAes = order.getMobileAes();
        String mobileMingWen = "";
        try {
            mobileMingWen = AES.decrypt(BizResourcesUtil.SECURITY_AES_KEY, mobileAes);

        } catch (Exception e) {
            logger.error("mobile decrypt error", e);
        }
        requestParam.put(YQConstants.Account, mobileMingWen);
        requestParam.put(YQConstants.CardID, YQConstants.RECHARGE_CARD_ID);
        requestParam.put(YQConstants.Amount, order.getAmount() + "");
        requestParam.put(YQConstants.NotifyUrl, vendorDO.getNotifyUrl());
        String sign = getSignature(requestParam, BizResourcesUtil.YQ_MD5_KEY);
        requestParam.put(YQConstants.Sign, sign);
        return requestParam;
    }

    /**
     * 根据订单构建订单查询的请求
     * 
     * @param order
     * @return
     */
    private Map<String, String> buildReOrderQueryRequestParam(OrderDO order, VendorDO vendorDO) {
        Map<String, String> requestParam = new TreeMap<String, String>();
        requestParam.put(YQConstants.Code, vendorDO.getVendorCode());
        String orderId = order.getOrderId();
        requestParam.put(YQConstants.OrderID, orderId);
        requestParam.put(YQConstants.CardID, YQConstants.RECHARGE_CARD_ID);
        String sign = getSignature(requestParam, BizResourcesUtil.YQ_MD5_KEY);
        requestParam.put(YQConstants.Sign, sign);
        return requestParam;
    }

    /**
     * 根据请求参数获取签名
     * 
     * @param requestParam
     * @param md5Key
     * @return
     */
    private String getSignature(Map<String, String> requestParam, String md5Key) {
        String params = Md5ParamsGenerator.generateMd5Params(requestParam);
        StringBuffer sigatureBody = new StringBuffer();
        sigatureBody.append(params).append(StringUtils.isNotBlank(md5Key) ? md5Key : "");
        return DigestUtils.md5Hex(sigatureBody.toString().trim()).toLowerCase();
    }
}
