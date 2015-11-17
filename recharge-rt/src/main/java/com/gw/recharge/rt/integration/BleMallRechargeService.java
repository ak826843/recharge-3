/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.integration;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gw.recharge.dal.model.OrderDO;
import com.gw.recharge.dal.model.VendorDO;
import com.gw.recharge.rt.integration.dto.RechargeResponseDto;
import com.gw.recharge.rt.service.OrderStatus;
import com.gw.recharge.rt.util.BizResourcesUtil;
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.recharge.rt.util.MobileAreaOpetator;
import com.gw.steel.steel.util.common.DateUtils;
import com.gw.steel.steel.util.httpclient.HttpClient;
import com.gw.steel.steel.util.security.AES;
import com.gw.steel.steel.util.security.MD5;
import com.gw.steel.steel.util.security.StringUtil;

/**
 * 
 * @author log.yin
 * @version $Id: BleMallRechargeService.java, v 0.1 2015年5月7日 下午8:28:27 log.yin Exp $
 */
public class BleMallRechargeService extends AbstractRefRechargeService {
    private static final Logger logger = LoggerFactory.getLogger(BleMallRechargeService.class);

    @Override
    protected String callRemoteCharge(OrderDO order, VendorDO vendorDO) {
        String url = BizResourcesUtil.BleMall_RECHARGE_URL;
        Map<String, String> requestParamMap = buildReChargeRequestParam(order, vendorDO);
        if (requestParamMap == null) {
            return null;
        }
        String queryString = getQueryString(requestParamMap);
        String requestStr = url + queryString;
        String respStr = "";
        try {
            logger.info("BleMall recharge request: {}", requestStr);
            respStr = HttpClient.get(requestStr);
            logger.info("BleMall recharge response: {}", respStr);
        } catch (Exception e) {
            logger.error("", e);
        }
        return respStr;
    }

    @Override
    protected String callRemoteQuery(OrderDO order, VendorDO vendorDO) {
        String url = BizResourcesUtil.BleMall_QUERY_URL;
        Map<String, String> requestParamMap = buildQueryRequestParam(order, vendorDO);
        if (requestParamMap == null) {
            return null;
        }
        String queryString = getQueryString(requestParamMap);
        String requestStr = url + queryString;
        String respStr = "";
        try {
            logger.info("BleMall query request: {}", StringUtil.ignoreMobile(requestStr, "mobile=", 11));
            respStr = HttpClient.get(requestStr);
            logger.info("BleMall query response: {}", respStr);
        } catch (Exception e) {
            logger.error("", e);
        }
        return respStr;
    }

    @Override
    protected RechargeResponseDto handleRechargeResponse(String responseStr) {
        RechargeResponseDto dto = new RechargeResponseDto();
        try {
            JSONObject jsonObject = JSON.parseObject(responseStr);
            String code = jsonObject.getString("code");
            String msg = jsonObject.getString("msg");//gb2312编码
            //        String sign = jsonObject.getString("sign");
            dto.setRefRespCode(code);
            dto.setRefRespMsg(msg);
            dto.setOrderId("");//服务商未提供orderID
            dto.setCode(CodeConstants.SUCCESS);
            if ("01".equals(code)) {
                dto.setOrderStatus(OrderStatus.UNDERWAY);
            } else {
                dto.setOrderStatus(OrderStatus.FAILED);
            }
        } catch (Exception e) {
            dto.setOrderStatus(OrderStatus.UNDERWAY);
        }
        return dto;
    }

    @Override
    protected RechargeResponseDto handleQueryResponse(String responseStr) {
        RechargeResponseDto dto = new RechargeResponseDto();
        try {
            JSONObject jsonObject = JSON.parseObject(responseStr);
            String code = jsonObject.getString("code");
            String msg = jsonObject.getString("msg");//gb2312编码
            /* String zflx = jsonObject.getString("zflx");
             String zfje = jsonObject.getString("zfje");
             String sign = jsonObject.getString("sign");*/
            dto.setRefRespCode(code);
            dto.setRefRespMsg(msg);

            dto.setCode(CodeConstants.SUCCESS);
            String orderId = "";
            if ("01".equals(code)) {
                dto.setOrderStatus(OrderStatus.SUCCESS);
                //msg=订单[123456782015051300004]充值成功,百联订单[15051302106442]
                int startIndex = msg.indexOf("百联订单[") + 5;
                orderId = msg.substring(startIndex, msg.length() - 1);
            } else if ("-6".equals(code) || "-9".equals(code)) {
                dto.setOrderStatus(OrderStatus.UNDERWAY);
            } else {
                dto.setOrderStatus(OrderStatus.FAILED);
            }
            dto.setOrderId(orderId);

        } catch (Exception e) {
            dto.setOrderStatus(OrderStatus.UNDERWAY);
        }
        return dto;
    }

    private Map<String, String> buildReChargeRequestParam(OrderDO order, VendorDO vendorDO) {
        Map<String, String> requestParam = new TreeMap<String, String>();
        requestParam.put("dshh", vendorDO.getVendorCode());
        requestParam.put("outorderid", order.getOrderId());
        String mobileMingWen = "";
        try {
            mobileMingWen = AES.decrypt(BizResourcesUtil.SECURITY_AES_KEY, order.getMobileAes());
        } catch (Exception e) {
            logger.error("mobile decrypt error", e);
        }
        requestParam.put("mobile", mobileMingWen);

        String faceValue = "";
        String dtype = "";
        String mobileOperator = MobileAreaOpetator.queryMobileAreaOpetator(mobileMingWen);
        //手机号归属地及面值参数取值路由
        if (MobileAreaOpetator.SH_CMCC.equals(mobileOperator)) {
            dtype = "00";
            if (order.getAmount() == 50) {
                faceValue = "1";

            } else if (order.getAmount() == 100) {
                faceValue = "2";
            } else if (order.getAmount() == 30) {
                faceValue = "0.6";
            }
        } else if (MobileAreaOpetator.SH_UNICOM.equals(mobileOperator)) {
            dtype = "01";
            faceValue = order.getAmount() / 10 + "";
        } else {//default
            dtype = "09";
            faceValue = order.getAmount() + "";
        }

        requestParam.put("dczsl", faceValue);//面值
        requestParam.put("dtype", dtype);
        String zflx = "";
        requestParam.put("zflx", zflx);
        requestParam.put("msgtype", "json");

        String sdsj = null;
        try {
            sdsj = DateUtils.format(order.getBeginTime(), "yyyyMMddHHmmss");
        } catch (Exception e) {
            sdsj = DateUtils.format(new Date(), "yyyyMMddHHmmss");
            logger.error("", e);
        }
        requestParam.put("sdsj", sdsj);
        //md5(dshh+outorderid+mobile+dczsl+dtype+zflx+msgtype+key)
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(requestParam.get("dshh"));
        sbBuffer.append(requestParam.get("outorderid"));
        sbBuffer.append(requestParam.get("mobile"));
        sbBuffer.append(requestParam.get("dczsl"));
        sbBuffer.append(requestParam.get("dtype"));
        sbBuffer.append(requestParam.get("zflx"));
        sbBuffer.append(requestParam.get("msgtype"));
        sbBuffer.append(BizResourcesUtil.BleMall_MD5_KEY);
        String sign = MD5.md5Hex(sbBuffer.toString());
        logger.info("sign={}, signBody={}", sign, sbBuffer.toString());
        requestParam.put("sign", sign);

        return requestParam;
    }

    private Map<String, String> buildQueryRequestParam(OrderDO order, VendorDO vendorDO) {
        Map<String, String> requestParam = new TreeMap<String, String>();
        requestParam.put("dshh", vendorDO.getVendorCode());
        requestParam.put("outorderid", order.getOrderId());
        requestParam.put("msgtype", "json");
        //md5(dshh+outorderid+msgtype +key)
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(requestParam.get("dshh"));
        sbBuffer.append(requestParam.get("outorderid"));
        sbBuffer.append(requestParam.get("msgtype"));
        sbBuffer.append(BizResourcesUtil.BleMall_MD5_KEY);
        String sign = MD5.md5Hex(sbBuffer.toString());
        logger.info("sign={}, signBody={}", sign, sbBuffer.toString());
        requestParam.put("sign", sign);

        return requestParam;
    }
}
