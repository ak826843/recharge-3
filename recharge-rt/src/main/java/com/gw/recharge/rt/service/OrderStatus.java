/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.service;

/**
 * 
 * @author log.yin
 * @version $Id: OrderStatus.java, v 0.1 2015年2月5日 上午11:00:43 log.yin Exp $
 */
public class OrderStatus {
    //订单初始状态
    public static final String INIT            = "INIT";
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
