package com.gw.recharge.rt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gw.recharge.dal.mapper.VendorDOMapper;
import com.gw.recharge.dal.model.VendorDO;

@Service("vendorService")
public class VendorService {

    @Autowired
    private VendorDOMapper vendorDOMapper;

    public VendorDO selectByPrimaryKey(String vendorId) {
        return vendorDOMapper.selectByPrimaryKey(vendorId);
    }

}
