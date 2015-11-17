/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.integration.dto.yq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author log.yin
 * @version $Id: YQReChargeResponseDto.java, v 0.1 2015年2月3日 上午10:38:04 log.yin Exp $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class YQReChargeResponseDto {

	@XmlElement(name = "OrderID")
	private String orderId;
	@XmlElement(name = "OrderStatus")
	private String orderStatus;
	@XmlElement(name = "failCode")
	private String failCode;
	@XmlElement(name = "failDesc")
	private String failDesc;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getFailCode() {
		return failCode;
	}

	public void setFailCode(String failCode) {
		this.failCode = failCode;
	}

	public String getFailDesc() {
		return failDesc;
	}

	@Override
	public String toString() {
		return "YQChargeResponseDto [orderId=" + orderId + ", orderStatus="
				+ orderStatus + ", failCode=" + failCode + ", failDesc="
				+ failDesc + "]";
	}

	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}

}
