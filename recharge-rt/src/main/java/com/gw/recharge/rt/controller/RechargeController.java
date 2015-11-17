/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller;

import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.gw.recharge.rt.controller.dto.RechargeRequest;
import com.gw.recharge.rt.controller.dto.RechargeResponse;
import com.gw.recharge.rt.service.RechargeService;
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.steel.steel.util.security.StringUtil;

/**
 * 充值
 * @author log.yin
 * @version $Id: RechargeController.java, v 0.1 2015年2月3日 上午10:28:36 log.yin Exp $
 */
@Controller
public class RechargeController extends BaseController<RechargeRequest, RechargeResponse> {
    private static final Logger logger = LoggerFactory.getLogger(RechargeController.class);

    @Autowired
    private ChainBase           checkCodeChainBase;

    @Autowired
    private RechargeService     rechargeService;

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    protected @ResponseBody
    RechargeResponse recharge(@RequestBody RechargeRequest req) {
        logger.info("RechargeRequest= {}",
            StringUtil.ignoreMobile(JSON.toJSONString(req), "\"mobile\":", 14));

        RechargeResponse resp = invoke(req);

        logger.info("RechargeResponse= {}",
            StringUtil.ignoreMobile(JSON.toJSONString(resp), "\"mobile\":", 14));
        return resp;
    }

    @Override
    public RechargeResponse execute(RechargeRequest req) {
        RechargeResponse resp = new RechargeResponse();
        try {
            final ContextBase context = new ContextBase();
            context.put("clientNo", req.getClientNo());
            context.put("operaterTime", req.getOperaterTime());
            context.put("mobile", req.getMobile());
            context.put("bizType", req.getBizType());
            context.put("amount", req.getAmount());
            context.put("osn", req.getOsn());

            checkCodeChainBase.execute(context);
            if (context.containsKey("code")) {
                resp.setCode(context.get("code").toString());
                if (context.containsKey("message")) {
                    resp.setMessage(context.get("message").toString());
                }
            } else {
                resp = rechargeService.recharge(req);
            }

            fillOtherSameParams(req, resp);
        } catch (Exception e) {
            resp.setCode(CodeConstants.UNKNOWN_ERROR);
            logger.error("", e);
        }

        return resp;
    }

    @Override
    public RechargeResponse handleCommonRequestParams(RechargeRequest req) {
        RechargeResponse resp = new RechargeResponse();
        fillOtherSameParams(req, resp);
        return resp;
    }

    private void fillOtherSameParams(RechargeRequest req, RechargeResponse resp) {
        resp.setClientNo(req.getClientNo());
        resp.setMobile(req.getMobile());
        resp.setAmount(req.getAmount());
        resp.setOsn(req.getOsn());
    }
}
