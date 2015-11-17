package com.gw.recharge.rt.integration;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.gw.recharge.dal.model.OrderDO;
import com.gw.recharge.dal.model.VendorDO;
import com.gw.recharge.rt.integration.dto.RechargeResponseDto;
import com.gw.recharge.rt.util.BizConstants;
import com.gw.recharge.rt.util.OrderIdGenerator;
import com.gw.steel.spring.rediscache.RedisCacheEngine;
import com.gw.steel.steel.util.common.DateUtils;

public abstract class AbstractRefRechargeService implements RefRechargeService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRefRechargeService.class);

    protected abstract String callRemoteCharge(OrderDO order, VendorDO vendorDO);

    protected abstract String callRemoteQuery(OrderDO order, VendorDO vendorDO);

    protected abstract RechargeResponseDto handleRechargeResponse(String responseStr);

    protected abstract RechargeResponseDto handleQueryResponse(String responseStr);

    @Autowired
    private RedisCacheEngine redisCacheEngine;

    @Override
    public RechargeResponseDto recharge(OrderDO order, VendorDO vendorDO) {
        String respStr = callRemoteCharge(order, vendorDO);
        if (StringUtils.isBlank(respStr)) {
            return null;
        }

        return handleRechargeResponse(respStr);
    }

    @Override
    public RechargeResponseDto query(OrderDO order, VendorDO vendorDO) {
        String respStr = callRemoteQuery(order, vendorDO);
        if (StringUtils.isBlank(respStr)) {
            return null;
        }
        return handleQueryResponse(respStr);
    }

    public String generateOrderId(VendorDO vendor) {
        Date createDate = new Date();
        StringBuffer redisCacheKeySBuffer = new StringBuffer();
        redisCacheKeySBuffer.append(BizConstants.REDIS_KEY_RECHARGE).append(":")
            .append(BizConstants.REDIS_KEY_ORDER_ID).append(":")
            .append(DateUtils.format(createDate, "yyyyMMddHHmmss"));

        String orderPrefix = OrderIdGenerator
            .generateOrerPrefix(vendor.getVendorCode(), createDate);

        ShardedJedisPool pool = redisCacheEngine.getPool();
        ShardedJedis jedis = pool.getResource();
        Long sequence = 1l;
        String cacheKey = redisCacheKeySBuffer.toString();
        if (jedis.exists(cacheKey)) {
            sequence = jedis.incrBy(cacheKey, 1);
        } else {
            jedis.setex(cacheKey, 24 * 60 * 60, sequence.toString());
        }

        if (jedis != null) {
            pool.returnResource(jedis);
        }

        return orderPrefix + OrderIdGenerator.leftPadZero(sequence.toString(), 5);
    };

    protected String getQueryString(Map<String, String> requestParam) {
        StringBuffer sb = new StringBuffer();
        for (String paramName : requestParam.keySet()) {
            String paramValue = requestParam.get(paramName);
            sb.append(paramName).append("=");
            if (StringUtils.isNotBlank(paramValue)) {
                try {
                    sb.append(URLEncoder.encode(paramValue, BizConstants.DEFAULT_ENCODING));
                } catch (Exception e) {
                    sb.append("");//encode failed,set paramValue blank
                    logger.error("build query string error", e);
                }
            }
            sb.append("&");
        }
        String queryString = sb.toString();
        return queryString.substring(0, queryString.length() - 1);
    }

}
