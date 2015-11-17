package com.gw.recharge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gw.recharge.rt.integration.YQRechargeService;
import com.gw.recharge.rt.service.ClientService;

@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
public class RechargeServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired 
	ClientService clientService;
    @Autowired
    YQRechargeService rechargeService;

    @Test
    public void testGenerateOderId() throws Exception {
//        String orderId = rechargeService.generateOrderId();
//        System.out.println(orderId);
    	clientService.selectByClientNo("0001");
    }
    
}
