package com.gw.recharge.dal.mapper;

import com.gw.recharge.dal.model.ClientDO;

public interface ClientDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientDO record);

    int insertSelective(ClientDO record);

    ClientDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientDO record);

    int updateByPrimaryKey(ClientDO record);
}