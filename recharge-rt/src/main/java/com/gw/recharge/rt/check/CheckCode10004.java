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
import com.gw.steel.steel.util.common.DateUtils;

/**
 * 
 * @author log.yin
 * @version $Id: CheckCode00001.java, v 0.1 2015年2月3日 下午2:19:50 log.yin Exp $
 */
public class CheckCode10004 implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CheckCode10004.class);
    
    @Autowired
    private RedisCacheEngine    redisCacheEngine;

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(Context context) throws Exception {
        String operaterTime = context.get("operaterTime").toString();

        ShardedJedisPool pool = redisCacheEngine.getPool();
        ShardedJedis jedis = pool.getResource();
        final String cacheKey = BizConstants.REDIS_KEY_RECHARGE + ":"
                                + BizConstants.REDIS_KEY_INVALID_REQ + ":" + operaterTime;

        try {
            if (jedis.exists(cacheKey)) {
                context.put("code", CodeConstants.INVALID_REQ);
                return PROCESSING_COMPLETE;
            } else {
                long currentTime = System.currentTimeMillis();
                long operaterTimeLong = DateUtils.parse(operaterTime.toString(), "yyyyMMddHHmmss")
                    .getTime();
                //24小时,redis失效时间设为1年
                if (currentTime - operaterTimeLong > 24 * 60 * 60 * 1000) {
                    context.put("code", CodeConstants.INVALID_REQ);
                    logger.info("请求operaterTime在当前时间24小时之前");
                    jedis.setex(cacheKey, 3 * 365 * 24 * 60 * 60, "1");
                    return PROCESSING_COMPLETE;
                }
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
