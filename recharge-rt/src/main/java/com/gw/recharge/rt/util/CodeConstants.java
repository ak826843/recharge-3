/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2014 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.util;

import com.gw.steel.steel.web.constants.BaseCodeConstants;

/**
 * 
 * @author log.yin
 * @version $Id: CodeConstants.java, v 0.1 2014年12月29日 下午1:24:10 log.yin Exp $
 */
public class CodeConstants extends BaseCodeConstants{
    /*
        其他code定义参见BaseCodeConstants
    
    10001=不是有效的手机号
    10002=充值面额不支持
    10003=频繁请求
    10004=失效请求
    10005=重复请求
    
    */
    public final static String INVALID_MOBILE           = "10001";
    public final static String NOT_SUPPORTED_FACE_VALUE = "10002";
    public final static String REQ_OFTEN                = "10003";
    public final static String INVALID_REQ              = "10004";
    public final static String DULPLICATED_REQ          = "10005";

    
}
