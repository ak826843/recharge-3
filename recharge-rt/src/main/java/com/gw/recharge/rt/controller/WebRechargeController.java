/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gw.recharge.dal.mapper.OrderDOMapper;
import com.gw.recharge.rt.service.ClientService;
import com.gw.recharge.rt.service.VendorService;

/**
 * 
 * @author log.yin
 * @version $Id: WebRechargeController.java, v 0.1 2015年4月22日 下午3:10:55 log.yin Exp $
 */
@Controller
@RequestMapping("app-no-used")
public class WebRechargeController {

    @Autowired
    private VendorService          vendorService;

    @Autowired
    private OrderDOMapper          orderDOMapper;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ClientService          clientService;

    @RequestMapping(value = "/webrecharge", method = RequestMethod.POST)
    public Object webRecharge(String secretKey, String mobiles, String faceValue,
                              HttpServletRequest request, HttpServletResponse response,
                              ModelAndView mov) {
        return null;
        
        /*logger.info("submit webrecharge");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("webrecharge-result");
        mv.addObject("message", "充值受理成功");

        if (StringUtils.isBlank(secretKey) || StringUtils.isBlank(mobiles)) {
            logger.info("secretKey or mobiles is blank");
            mv.setViewName("login");
            mv.addObject("message", "操作不合法");
            return mv;
        }

        ClientDO clientDO = clientService.selectByClientNo(BizResourcesUtil.WEBRECHARGE_CLIENT);
        if (clientDO == null || !clientDO.getClientKey().equals(secretKey)) {
            logger.info("Not found client config with the secretKey");
            mv.setViewName("login");
            mv.addObject("message", "密钥错误");
            return mv;
        }

        mobiles = mobiles.trim();
        if (",".equals(mobiles.charAt(mobiles.length() - 1))) {
            mobiles = mobiles.substring(0, mobiles.length() - 1);
        }
        String[] mobileArr = mobiles.split(",");

        final List<RechargeRequest> rechargeRequests = new ArrayList<RechargeRequest>();

        //default 10元
        int faceValueAmount = 10;
        try {
            faceValueAmount = Integer.parseInt(faceValue);
        } catch (Exception e) {
            logger.error("faceValueAmount is not valid", e);
        }

        int index = 0;
        for (String mobile : mobileArr) {
            final RechargeRequest rechargeRequest = new RechargeRequest();
            rechargeRequest.setVersion("1.0");
            rechargeRequest.setInputCharset("1");
            rechargeRequest.setSignType("1");
            rechargeRequest.setClientNo(BizResourcesUtil.WEBRECHARGE_CLIENT);
            rechargeRequest.setMobile(mobile.trim());
            rechargeRequest.setBizType(1);
            rechargeRequest.setAmount(faceValueAmount);
            rechargeRequest.setOsn(UUID.randomUUID().toString() + index);
            index++;
            rechargeRequest.setOperaterTime(DateUtils.format(new Date(), "yyyyMMddHHmmss"));
            String clientKey = clientService.selectByClientNo(BizResourcesUtil.WEBRECHARGE_CLIENT)
                .getClientKey();
            String signMsg = MD5SignUtil.getSignatureMsg(rechargeRequest, clientKey);
            rechargeRequest.setSignMsg(signMsg);
            //
            rechargeRequests.add(rechargeRequest);
            final String requestJsonString = JSON.toJSONString(rechargeRequest);
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("web recharge url={} ,request={} ",
                            BizResourcesUtil.WEBRECHARGE_URL,
                            StringUtil.ignoreMobile(requestJsonString, "\"mobile\":", 14));
                        String response = HttpClient.post(BizResourcesUtil.WEBRECHARGE_URL,
                            requestJsonString);
                        JSONObject json = (JSONObject) JSON.parse(response);
                        RechargeResponse rechargeResponse = new RechargeResponse();
                        rechargeResponse = JSONObject.toJavaObject(json, RechargeResponse.class);

                        logger.info("web recharge response: {}", StringUtil.ignoreMobile(
                            JSON.toJSONString(rechargeResponse), "\"mobile\":", 14));
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            });
        }
        return mv;*/
    }
}
