package com.gw.recharge.dal.mapper;

import java.util.List;

import com.gw.recharge.dal.model.OrderDO;

public interface OrderDOMapperExt {

    public List<OrderDO> queryOrdersQuery();

    public OrderDO selectByOrderId(String orderId);
}