package com.gw.recharge.rt.service;

public enum Vendor {
    /*// 意桥充值服务
	YQ_CHARGE("100001"),
	NSK_CHARGE("100002");*/

    BleMall_RECHARGE("100001"),
    NSK_CHARGE("100002");
    
	private String vendorId;

	private Vendor(String vendorId) {
        this.vendorId = vendorId;
	}
	
	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public static Vendor getInstance(String vendorCode) {
		for (Vendor vendor : Vendor.values()) {
			if (vendor.getVendorId().equals(vendorCode)) {
				return vendor;
			}
		}
		return null;
	}
}