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
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.steel.spring.rediscache.RedisCacheEngine;

/**
 * 
 * @author log.yin
 * @version $Id: CheckCode00001.java, v 0.1 2015年2月3日 下午2:19:50 log.yin Exp $
 */
public class CheckCode10005 implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CheckCode10005.class);

    @Autowired
    private RedisCacheEngine    redisCacheEngine;

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(Context context) throws Exception {
        Object osn = context.get("osn");
        String clientNo = context.get("clientNo").toString();

        if (osn != null && osn.toString().trim().length() > 0) {
            ShardedJedisPool pool = redisCacheEngine.getPool();
            ShardedJedis jedis = pool.getResource();
            final String cacheKey = BizConstants.REDIS_KEY_RECHARGE + ":"
                                    + BizConstants.REDIS_KEY_DUPLICATED_REQ + ":" + clientNo + "-"
                                    + osn;

            try {
                if (jedis.exists(cacheKey)) {
                    context.put("code", CodeConstants.DULPLICATED_REQ);
                    return PROCESSING_COMPLETE;
                } else {//3 years
                    jedis.setex(cacheKey, 3 * 365 * 24 * 60 * 60, "1");
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                if (jedis != null) {
                    pool.returnResource(jedis);
                }
            }
        }
        return CONTINUE_PROCESSING;
    }

}
