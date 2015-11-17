package com.gw.recharge.dal.model;

import java.util.Date;

public class VendorDO {
    private String vendorId;

    private String vendorCode;

    private String vendorDesc;

    private String notifyUrl;

    private Integer status;

    private Date uptTime;

    public String getVendorId() {
        return vendorId;
    }

	public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorDesc() {
        return vendorDesc;
    }

    public void setVendorDesc(String vendorDesc) {
        this.vendorDesc = vendorDesc;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUptTime() {
        return uptTime;
    }

    public void setUptTime(Date uptTime) {
        this.uptTime = uptTime;
    }
    
    @Override
 	public String toString() {
 		return "VendorDO [vendorId=" + vendorId + ", vendorCode=" + vendorCode
 				+ ", vendorDesc=" + vendorDesc + ", notifyUrl=" + notifyUrl
 				+ ", status=" + status + "]";
 	}

}