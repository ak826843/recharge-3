package com.gw.recharge.dal.mapper;

import com.gw.recharge.dal.model.OrderDO;

public interface OrderDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDO record);

    int insertSelective(OrderDO record);

    OrderDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDO record);

    int updateByPrimaryKey(OrderDO record);
}