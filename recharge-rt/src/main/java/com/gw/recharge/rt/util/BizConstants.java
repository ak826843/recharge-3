/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.util;

/**
 * 
 * @author log.yin
 * @version $Id: BizConstants.java, v 0.1 2015年2月3日 上午10:50:43 log.yin Exp $
 */
public class BizConstants {
    //业务类型
    public static final String DEFAULT_ENCODING      = "UTF-8";

    //redis cache key
    public static final String REDIS_KEY_RECHARGE    = "RECHARGE";
    public static final String REDIS_KEY_INVALID_REQ = "INVALID_REQ";
    public static final String REDIS_KEY_OFTEN_REQ   = "OFTEN_REQ";
    public static final String REDIS_KEY_DUPLICATED_REQ   = "DUPLICATED_REQ";

    public static final String REDIS_KEY_ORDER_ID = "ORDER_ID";
    
    public static final String REDIS_KEY_CLIENT = "CLIENT";
    public static final String REDIS_KEY_VENDOR = "VENDOR";
}