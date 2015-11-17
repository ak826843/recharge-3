/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.service;

import java.security.NoSuchAlgorithmException;

import com.gw.steel.steel.util.httpclient.HttpClient;
import com.gw.steel.steel.util.security.MD5;

/**
 * 
 * @author log.yin
 * @version $Id: NskTest.java, v 0.1 2015年3月30日 下午3:18:54 log.yin Exp $
 */
public class NskTest {

    /**
     * 
     * @param args
     * @throws NoSuchAlgorithmException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        final String url = "http://218.204.136.50:8272/AddTelCharge.aspx?";
        StringBuffer queryString = new StringBuffer();

        //
        String user = "shdzh";
        String passwd = "shdzh0325";
        String signBody = user + passwd;
        //        BASE64Encoder base64en = new BASE64Encoder();
        //        Base64 base64=new Base64();
        //        passwd= base64.encodeToString(md5.digest(signBody.getBytes()));
        //        
        System.err.println("signBody: \n" + signBody);

        passwd = MD5.md5Hex(signBody);

        //        Base64e base64=new Base64();
        // 加密后的字符串
        String mobiles = "18616361111";
        String money = "100";

        queryString.append("user").append("=").append(user).append("&");
        queryString.append("passwd").append("=").append(passwd).append("&");
        queryString.append("mobiles").append("=").append(mobiles).append("&");
        queryString.append("money").append("=").append(money).append("&");
        queryString.append("dtype").append("=").append("0");

        String requestStr = url + queryString.toString();
        String respStr = "";
        try {
            System.err.println("request: \n" + requestStr);
            respStr = HttpClient.get(requestStr);
            System.err.println("response: \n" + respStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
