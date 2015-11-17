package com.gw.recharge.dal.mapper;

import com.gw.recharge.dal.model.VendorDO;

public interface VendorDOMapper {
    int deleteByPrimaryKey(String vendorId);

    int insert(VendorDO record);

    int insertSelective(VendorDO record);

    VendorDO selectByPrimaryKey(String vendorId);

    int updateByPrimaryKeySelective(VendorDO record);

    int updateByPrimaryKey(VendorDO record);
}