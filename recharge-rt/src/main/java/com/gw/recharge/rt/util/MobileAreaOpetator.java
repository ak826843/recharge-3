/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gw.recharge.rt.integration.dto.MobileAreaInfo;
import com.gw.steel.steel.util.httpclient.HttpClient;

/**
 * 
 * @author log.yin
 * @version $Id: MobileAreaOpetator.java, v 0.1 2015年5月11日 下午4:57:07 log.yin Exp $
 */
public class MobileAreaOpetator {
    private static final Logger logger     = LoggerFactory.getLogger(MobileAreaOpetator.class);

    public final static String  SH_UNICOM  = "上海联通";
    public final static String  SH_CMCC    = "上海移动";
    public final static String  OTHER_CITY = "全国";

    public static String queryMobileAreaOpetator(String mobile) {
        final String url = BizResourcesUtil.QUERY_MOBILE_AREA_URL;
        //        String url = "https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=";

        try {
            String response = HttpClient.get(url + mobile);

            int startIndex = response.indexOf("{");
            int endIndex = response.lastIndexOf("}");

            String jsonString = response.substring(startIndex, endIndex + 1);
            logger.info("queryMobileAreaOpetator: {}", jsonString);

            JSONObject json = (JSONObject) JSON.parse(jsonString);

            MobileAreaInfo mobileInfo = JSON.toJavaObject(json, MobileAreaInfo.class);
            logger.info(JSON.toJSONString(mobileInfo, true));
            //            System.err.println(JSON.toJSONString(mobileInfo, true));

            return mobileInfo.getData().getArea_operator();
        } catch (Exception e) {
            logger.error("", e);
        }

        //default
        return MobileAreaOpetator.OTHER_CITY;
    }

    public static void main(String[] args) {
        MobileAreaOpetator.queryMobileAreaOpetator("18616365467");

        Integer faceValue = 30;
        System.err.println(faceValue / 10 + "");
    }

}
