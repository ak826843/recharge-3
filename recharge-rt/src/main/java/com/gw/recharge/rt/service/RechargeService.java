package com.gw.recharge.rt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.gw.recharge.dal.mapper.OrderDOMapper;
import com.gw.recharge.dal.mapper.OrderDOMapperExt;
import com.gw.recharge.dal.model.OrderDO;
import com.gw.recharge.dal.model.VendorDO;
import com.gw.recharge.rt.controller.dto.RechargeRequest;
import com.gw.recharge.rt.controller.dto.RechargeResponse;
import com.gw.recharge.rt.integration.AbstractRefRechargeService;
import com.gw.recharge.rt.integration.dto.RechargeResponseDto;
import com.gw.recharge.rt.util.BizConstants;
import com.gw.recharge.rt.util.BizResourcesUtil;
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.recharge.rt.util.OrderIdGenerator;
import com.gw.steel.spring.rediscache.RedisCacheEngine;
import com.gw.steel.steel.util.common.DateUtils;
import com.gw.steel.steel.util.security.AES;
import com.gw.steel.steel.util.security.MD5;
import com.gw.steel.steel.util.security.StringUtil;

/**
 * 充值服务接口
 * 
 * @author log.yin
 * 
 */
@Service
public class RechargeService {

    private static final Logger                     logger = LoggerFactory
                                                               .getLogger(RechargeService.class);
    @Autowired
    private OrderDOMapperExt                        orderDOMapperExt;

    @Autowired
    private OrderDOMapper                           orderDOMapper;

    @Autowired
    private ThreadPoolTaskExecutor                  taskExecutor;

    @Autowired
    private VendorService                           vendorService;

    @Autowired
    private Map<String, AbstractRefRechargeService> refRechargeServiceMap;

    @Autowired
    private RedisCacheEngine                        redisCacheEngine;

    public RechargeResponse recharge(RechargeRequest req) throws Exception {
        RechargeResponse response = new RechargeResponse();

        // 初始化订单
        final OrderDO orderDO = generateOrder(req);

        Map<String, Boolean> calledVendorMap = new HashMap<String, Boolean>();
        Vendor defaultVendor = routeVendor(orderDO, true, calledVendorMap);

        VendorDO vendorDO = vendorService.selectByPrimaryKey(defaultVendor.getVendorId());
        if (vendorDO == null || vendorDO.getStatus() == 0) {
            logger.warn("检查 vendorId={} 配置和缓存", defaultVendor.getVendorId());
            response.setCode(CodeConstants.UNKNOWN_ERROR);
            return response;
        }

        //订单号check
        String orderId = generateOrderId(vendorDO);
        if (orderId == null) {
            logger.warn("订单号生成失败");
            response.setCode(CodeConstants.UNKNOWN_ERROR);
            return response;
        }
        orderDO.setOrderId(orderId);
        orderDO.setVendorId(defaultVendor.getVendorId());

        // 订单落地
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                logger.info("save order : orderId={}, vendorId={}, clientNo={}", new String[] {
                        orderDO.getOrderId(), orderDO.getVendorId(), orderDO.getClientNo() });

                orderDOMapper.insert(orderDO);

                logger.info("save order successd: id={}, orderId={} ",
                    new String[] { orderDO.getId() + "", orderDO.getOrderId() });
            }
        });

        boolean rechargeCompeleted = true;

        RechargeResponseDto refChargeRespDto = null;

        Vendor vendor = defaultVendor;

        do {
            rechargeCompeleted = true;
            if (vendor == null) {
                logger.warn("无可使用的vendor，充值失败");
                response.setCode(CodeConstants.UNKNOWN_ERROR);
                return response;
            }

            String vendorId = vendorDO.getVendorId();
            AbstractRefRechargeService refRechargeService = refRechargeServiceMap.get(vendorId);
            if (refRechargeService == null) {
                logger.warn("未配置vendorId={}的服务", orderDO.getVendorId());
                response.setCode(CodeConstants.UNKNOWN_ERROR);
                return response;
            }

            //
            orderDO.setBeginTime(new Date());

            refChargeRespDto = refRechargeService.recharge(orderDO, vendorDO);
            //
            //            String refCode = refChargeRespDto.getRefRespCode();

            //切换
            /*if (Vendor.NSK_CHARGE == vendor && !"0".equals(refCode)) {
                logger.info("refCode: {}", refCode);
                if ("6".equals(refCode) || "7".equals(refCode) || "8".equals(refCode)
                    || "9".equals(refCode)) {
                    //该vendor已经被调用过
                    calledVendorMap.put(vendorDO.getVendorId(), true);
                    rechargeCompeleted = false;
                    vendor = routeVendor(orderDO, false, calledVendorMap);
                    logger.info("vendorId={}充值服务切换到 vendorId={}", vendorDO.getVendorId(),
                        vendor.getVendorId());
                    vendorDO = vendorService.selectByPrimaryKey(vendor.getVendorId());
                }
            }*/

        } while (!rechargeCompeleted);

        if (refChargeRespDto == null) {
            response.setCode(CodeConstants.UNKNOWN_ERROR);
            logger.info("充值接口返回为null，vendorId={},orderId={}", orderDO.getVendorId(),
                orderDO.getOrderId());
            return response;
        }

        logger.info("调用第三方充值服务返回结果: vendorId={}, resp={}", orderDO.getVendorId(), refChargeRespDto);
        response.setCode(refChargeRespDto.getCode());

        //如果切换vendor， 最终的订单号和vendor
        if (defaultVendor != vendor) {
            orderDO.setOrderId(generateOrderId(vendorDO));
            orderDO.setVendorId(vendor.getVendorId());
        }

        //更新订单信息
        final RechargeResponseDto refChargeRespDto2 = refChargeRespDto;
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                updateOrderStatus(orderDO, refChargeRespDto2);
            }
        });

        return response;
    }

    public RechargeResponseDto queryOrder(OrderDO order) {
        AbstractRefRechargeService refRechargeService = refRechargeServiceMap.get(order
            .getVendorId());
        if (refRechargeService == null) {
            logger.warn("未配置vendorId={}的服务", order.getVendorId());
            return null;
        }

        VendorDO vendorDO = vendorService.selectByPrimaryKey(order.getVendorId());
        return refRechargeService.query(order, vendorDO);
    }

    private Vendor routeVendor(OrderDO order, boolean isDdefaultVendor,
                               Map<String, Boolean> calledVendorMap) {

        return Vendor.getInstance(BizResourcesUtil.DEFAULT_VENDOR_ID);
    }

    private void updateOrderStatus(OrderDO orderDO, RechargeResponseDto refChargeRespDto) {
        if (refChargeRespDto != null) {
            orderDO.setUptTime(new Date());
            orderDO.setEndTime(new Date());
            orderDO.setOrderStatus(refChargeRespDto.getOrderStatus());
            orderDO.setRespCode(refChargeRespDto.getRefRespCode());
            orderDO.setRespMsg(refChargeRespDto.getRefRespMsg());
            orderDO.setVendorOrderNo(refChargeRespDto.getOrderId());
            logger
                .info(
                    "recharge completed, update order : id={}, orderId={}, vendorId={}, orderStatus={}, respCode={}",
                    new String[] { orderDO.getId() + "", orderDO.getOrderId(),
                            orderDO.getVendorId(), orderDO.getOrderStatus(),
                            refChargeRespDto.getRefRespCode() });
            orderDOMapper.updateByPrimaryKeySelective(orderDO);
        }
    }

    private OrderDO generateOrder(RechargeRequest req) throws Exception {
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderId("");
        orderDO.setVendorId("");
        orderDO.setAmount(req.getAmount());
        orderDO.setBizType(req.getBizType());
        orderDO.setClientNo(req.getClientNo());
        orderDO.setCrtTime(new Date());
        orderDO.setOsn(req.getOsn());
        String mobile = req.getMobile();
        String mobileAes = "";
        String mobileMd5 = "";
        String mobileMark = StringUtil.maskMobilePhone(mobile);
        orderDO.setOperatorTime(DateUtils.parse(req.getOperaterTime(), "yyyyMMddHHmmss"));
        try {
            mobileMd5 = MD5.md5Hex(mobile, BizResourcesUtil.YQ_MD5_KEY);
            mobileAes = AES.encrypt(BizResourcesUtil.SECURITY_AES_KEY,
                BizResourcesUtil.SECURITY_AES_KEY, mobile);
        } catch (Exception e) {
            logger.error("mobile encrypt error", e);
        }

        orderDO.setMobileAes(mobileAes);
        orderDO.setOrderStatus(OrderStatus.INIT);
        orderDO.setUptTime(new Date());
        orderDO.setMobileMark(mobileMark);
        orderDO.setMobileMd5(mobileMd5);
        return orderDO;
    }

    public String generateOrderId(VendorDO vendorDO) {
        Date createDate = new Date();
        StringBuffer redisCacheKeySBuffer = new StringBuffer();
        redisCacheKeySBuffer.append(BizConstants.REDIS_KEY_RECHARGE).append(":")
            .append(BizConstants.REDIS_KEY_ORDER_ID).append(":")
            .append(DateUtils.format(createDate, "yyyyMMdd"));

        String orderPrefix = OrderIdGenerator.generateOrerPrefix(vendorDO.getVendorCode(),
            createDate);

        ShardedJedisPool pool = redisCacheEngine.getPool();
        ShardedJedis jedis = pool.getResource();
        Long sequence = 1l;
        String cacheKey = redisCacheKeySBuffer.toString();

        try {
            sequence = jedis.incr(cacheKey);
            return orderPrefix + OrderIdGenerator.leftPadZero(sequence.toString(), 5);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return null;
    }

    public void setRefRechargeServiceMap(Map<String, AbstractRefRechargeService> refRechargeServiceMap) {
        this.refRechargeServiceMap = refRechargeServiceMap;
    }
}
