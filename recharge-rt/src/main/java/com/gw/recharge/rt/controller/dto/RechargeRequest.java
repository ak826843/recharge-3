/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller.dto;

import java.util.LinkedHashMap;

import com.gw.steel.steel.web.controller.dto.BaseRequest;

/**
 * 
 * @author log.yin
 * @version $Id: RechargeRequest.java, v 0.1 2015年2月4日 上午10:31:54 log.yin Exp $
 */
public class RechargeRequest extends BaseRequest {
    /**  */
    private static final long serialVersionUID = 6153497699693870821L;

    private String            mobile;
    private int               bizType;
    private int               amount;
    private String            osn;
    private String            operaterTime;

    /** 
     * @see com.gw.recharge.rt.controller.dto.BaseRequest#getBizSignatureParamMap()
     */
    @Override
    protected LinkedHashMap<String, String> buildBizSignatureParamMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("mobile", mobile);
        map.put("bizType", bizType + "");
        map.put("amount", amount + "");
        map.put("osn", osn);
        map.put("operaterTime", operaterTime);
        return map;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOsn() {
        return osn;
    }

    public void setOsn(String osn) {
        this.osn = osn;
    }

    public String getOperaterTime() {
        return operaterTime;
    }

    public void setOperaterTime(String operaterTime) {
        this.operaterTime = operaterTime;
    }
}
