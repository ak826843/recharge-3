package com.gw.recharge.rt.integration;

import com.gw.recharge.dal.model.OrderDO;
import com.gw.recharge.dal.model.VendorDO;
import com.gw.recharge.rt.integration.dto.RechargeResponseDto;

/**
 * 调用第三方统一的抽象接口
 * @author gaowei
 *
 */
public interface RefRechargeService {

    public RechargeResponseDto recharge(OrderDO order, VendorDO vendorDO);

    public RechargeResponseDto query(OrderDO order,VendorDO vendorDO);

}
