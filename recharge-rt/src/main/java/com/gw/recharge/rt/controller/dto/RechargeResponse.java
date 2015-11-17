/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller.dto;

import java.util.LinkedHashMap;

import com.gw.steel.steel.web.controller.dto.BaseResponse;

/**
 * 
 * @author log.yin
 * @version $Id: RechargeResponse.java, v 0.1 2015年2月4日 上午10:54:10 log.yin Exp $
 */
public class RechargeResponse extends BaseResponse {

    /**  */
    private static final long serialVersionUID = -7563507093387156192L;
    private String            clientNo;
    private String            mobile;
    private int               amount;
    private String            osn;

    /** 
     * @see com.gw.recharge.rt.controller.dto.BaseResponse#getBizSignatureParamMap()
     */
    @Override
    protected LinkedHashMap<String, String> buildBizSignatureParamMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("clientNo", clientNo);
        map.put("mobile", mobile);
        map.put("amount", amount + "");
        map.put("osn", osn);
        map.put("code", getCode());
        map.put("message", getMessage());
        return map;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

}
