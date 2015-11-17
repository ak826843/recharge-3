/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.type;

/**
 * 
 * @author log.yin
 * @version $Id: BizType.java, v 0.1 2015年2月3日 下午7:17:41 log.yin Exp $
 */
public enum BizType {
    RECHARGE(1, "100"),

    UNKNOWN(9999, "9999");

    private int code;
    private String value;

    private BizType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
