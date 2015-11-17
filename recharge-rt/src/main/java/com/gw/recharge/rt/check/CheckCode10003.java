/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.check;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.gw.recharge.rt.util.BizConstants;
import com.gw.recharge.rt.util.BizResourcesUtil;
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.steel.spring.rediscache.RedisCacheEngine;
import com.gw.steel.steel.util.security.MD5;

/**
 * 
 * @author log.yin
 * @version $Id: CheckCode00001.java, v 0.1 2015年2月3日 下午2:19:50 log.yin Exp $
 */
public class CheckCode10003 implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CheckCode10003.class);

    @Autowired
    private RedisCacheEngine    redisCacheEngine;

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(Context context) throws Exception {
        String clientNo = context.get("clientNo").toString();
        String mobile = context.get("mobile").toString();
        
        ShardedJedisPool pool = redisCacheEngine.getPool();
        ShardedJedis jedis = pool.getResource();
        String mobileMd5 = MD5.md5Hex(mobile,  BizResourcesUtil.YQ_MD5_KEY);
        final String cacheKey = BizConstants.REDIS_KEY_RECHARGE + ":"
                                + BizConstants.REDIS_KEY_OFTEN_REQ + ":" + clientNo + "-"
                                + mobileMd5;
        logger.info("OFTEN_REQ, cacheKey={}", cacheKey);
        try {
            if (jedis.exists(cacheKey)) {
                context.put("code", CodeConstants.REQ_OFTEN);
                return PROCESSING_COMPLETE;
            } else {//5分钟
                jedis.setex(cacheKey, 5 * 60, "1");
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }

        return CONTINUE_PROCESSING;
    }

}
