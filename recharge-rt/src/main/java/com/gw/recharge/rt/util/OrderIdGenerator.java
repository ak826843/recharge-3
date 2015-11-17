/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.util;

import java.util.Date;

import org.springframework.util.Assert;

import com.gw.steel.steel.util.common.DateUtils;

/**
 * 
 * @author log.yin
 * @version $Id: OrderIdGenerator.java, v 0.1 2015年2月3日 下午1:26:13 log.yin Exp $
 */
public class OrderIdGenerator {
    /**
     * 生成订单号前缀： 6位商户编号 + YYYYMMDDHHmmss
     * @return
     */
    public static String generateOrerPrefix(String merchantNo, Date date) {
        return merchantNo + DateUtils.format(date, "yyyyMMdd");
    }

    public static String leftPadZero(String source, int length) {
        Assert.notNull(source);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - source.length(); i++) {
            sb.append(0);
        }
        sb.append(source);
        return sb.toString();
    }
}
