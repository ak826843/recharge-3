package com.gw.recharge.rt.integration.dto;

public class RechargeResponseDto {

	private String code;
	private String message;
	private String orderId;
	private String orderStatus;
	private String refRespCode;
	private String refRespMsg;

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

	/**
	 * 调用第三方接口返回的状态码
	 * 
	 * @return
	 */
	public String getRefRespCode() {
		return refRespCode;
	}

	public void setRefRespCode(String refRespCode) {
		this.refRespCode = refRespCode;
	}

	/**
	 * 调用第三方接口返回的错误信息
	 * 
	 * @return
	 */
	public String getRefRespMsg() {
		return refRespMsg;
	}

	public void setRefRespMsg(String refRespMsg) {
		this.refRespMsg = refRespMsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RechargeResponseDto [code=" + code + ", message=" + message
				+ ", orderId=" + orderId + ", orderStatus=" + orderStatus
				+ ", refRespCode=" + refRespCode + ", refRespMsg=" + refRespMsg
				+ "]";
	}

}
