package com.gw.recharge.rt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gw.recharge.dal.mapper.ClientDOMapper;
import com.gw.recharge.dal.mapper.ClientDOMapperExt;
import com.gw.recharge.dal.model.ClientDO;

@Service
public class ClientService {

    @Autowired
    private ClientDOMapper    clientDOMapper;
    
    @Autowired
    private ClientDOMapperExt clientDOMapperExt;

    public ClientDO selectByClientNo(String clientNo) {
        return clientDOMapperExt.selectByClientNo(clientNo);
    }
}
