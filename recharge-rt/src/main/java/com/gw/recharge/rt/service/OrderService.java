/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gw.recharge.dal.mapper.OrderDOMapper;
import com.gw.recharge.dal.mapper.OrderDOMapperExt;
import com.gw.recharge.dal.model.OrderDO;
import com.gw.steel.steel.util.common.DateUtils;

/**
 * 
 * @author log.yin
 * @version $Id: OrderService.java, v 0.1 2015年2月5日 上午11:06:07 log.yin Exp $
 */
@Service("orderService")
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderDOMapperExt    orderDOMapperExt;

    @Autowired
    private OrderDOMapper       orderDOMapper;

    public OrderDO selectByOrderId(String orderId) {
        return orderDOMapperExt.selectByOrderId(orderId);
    }

    public void updateOrder(String merchantOrderId, String vendorOrderNo, String orderStatus,
                            String successTime) {
        OrderDO orderDo = orderDOMapperExt.selectByOrderId(merchantOrderId);
        logger
            .info("orderId:{}, current orderStatus:{}", orderDo.getId(), orderDo.getOrderStatus());

        // 只更新充值成功的订单
        if (OrderStatus.SUCCESS.equals(orderDo.getOrderStatus())) {
            logger.info("No need to update orderStatus, it's success");
            return;
        }

        OrderDO updatedOrder = new OrderDO();
        updatedOrder.setId(orderDo.getId());
        updatedOrder.setOrderId(merchantOrderId);
        updatedOrder.setOrderStatus(orderStatus);
        updatedOrder.setVendorOrderNo(vendorOrderNo);
        try {
            updatedOrder.setEndTime(DateUtils.parse(successTime, "yyyyMMddHHmmss"));
        } catch (Exception e) {
            updatedOrder.setEndTime(new Date());
        }
        orderDOMapper.updateByPrimaryKeySelective(updatedOrder);
    }

}
