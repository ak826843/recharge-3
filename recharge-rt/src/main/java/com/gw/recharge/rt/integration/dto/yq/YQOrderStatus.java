/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.integration.dto.yq;

/**
 * 
 * @author log.yin
 * @version $Id: YQOrderStatus.java, v 0.1 2015年2月5日 下午4:48:19 log.yin Exp $
 */
public class YQOrderStatus {
    //订单充值成功
    public static final String SUCCESS         = "SUCCESS";
    //订单充值失败
    public static final String FAILED          = "FAILED";
    //订单充值中
    public static final String UNDERWAY        = "UNDERWAY";
    //请求失败，需要重试
    public static final String REQUEST_FAILED  = "REQUEST_FAILED";
    //订单不存在
    public static final String ORDER_NOT_EXIST = "ORDER_NOT_EXIST";
    //订单取消
    public static final String CANCEL          = "CANCEL";
}
