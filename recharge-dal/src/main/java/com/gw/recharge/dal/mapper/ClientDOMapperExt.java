package com.gw.recharge.dal.mapper;

import com.gw.recharge.dal.model.ClientDO;


public interface ClientDOMapperExt {

    ClientDO selectByClientNo(String clientNo);
}