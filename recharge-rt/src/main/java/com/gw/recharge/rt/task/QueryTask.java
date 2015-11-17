/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.gw.recharge.dal.mapper.OrderDOMapper;
import com.gw.recharge.dal.mapper.OrderDOMapperExt;
import com.gw.recharge.dal.model.OrderDO;
import com.gw.recharge.rt.integration.dto.RechargeResponseDto;
import com.gw.recharge.rt.service.OrderStatus;
import com.gw.recharge.rt.service.RechargeService;

/**
 * 订单查询：ORDER_STATUS='UNDERWAY' ，3小时内
 * 
 * @author log.yin
 * @version $Id: QueryTask.java, v 0.1 2015年2月3日 上午10:29:53 log.yin Exp $
 */

public class QueryTask implements AbstractTask {
    private static final Logger logger = LoggerFactory.getLogger(QueryTask.class);

    @Autowired
    private OrderDOMapperExt    orderDOMapperExt;

    @Autowired
    private OrderDOMapper       orderDOMapper;

    @Autowired
    private RechargeService     rechargeService;

    @Override
    public void execute() {
        logger.info("Start recharge order status query task");

        //ORDER_STATUS='UNDERWAY' ，3小时内
        List<OrderDO> list = orderDOMapperExt.queryOrdersQuery();
        if (list == null || list.size() == 0) {
            logger.info("No data to be send,query task finished");
        }

        for (OrderDO orderDO : list) {
            logger.info("order-id: {}", orderDO.getId());
            RechargeResponseDto queryResponseDto = rechargeService.queryOrder(orderDO);
            logger.info("query response: {}", JSON.toJSONString(queryResponseDto));
            updateRecord(orderDO, queryResponseDto);
        }
    }

    private void updateRecord(OrderDO orderDO, RechargeResponseDto queryResponseDto) {
        if (queryResponseDto == null) {
            return;
        }

        logger.info("query result: orderId={}, orderStatus={}", orderDO.getOrderId(),
            queryResponseDto.getOrderStatus());

        if (OrderStatus.UNDERWAY.equals(queryResponseDto.getOrderStatus())) {
            logger.info("orderId={} orderStatus is UNDERWAY", orderDO.getOrderId());
            return;
        }

        if (OrderStatus.SUCCESS.equals(queryResponseDto.getOrderStatus())) {
            OrderDO updatedOorderDO = new OrderDO();
            updatedOorderDO.setId(orderDO.getId());
            updatedOorderDO.setOrderStatus(queryResponseDto.getOrderStatus());
            updatedOorderDO.setVendorOrderNo(queryResponseDto.getOrderId());
            updatedOorderDO.setRespCode(queryResponseDto.getRefRespCode());
            updatedOorderDO.setRespMsg(queryResponseDto.getRefRespMsg());
            //订单成功充值， 更新endtime
            updatedOorderDO.setEndTime(new Date());
            //充值成功之后需清除手机号
            updatedOorderDO.setMobileAes(null);
            updatedOorderDO.setUptTime(new Date());
            logger.info("updated record: {}", JSON.toJSONString(updatedOorderDO));
            orderDOMapper.updateByPrimaryKeySelective(updatedOorderDO);
        }
    }
}
