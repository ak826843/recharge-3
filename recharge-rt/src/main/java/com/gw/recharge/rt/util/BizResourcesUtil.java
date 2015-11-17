/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2014 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author log.yin
 * @version $Id: BizResourcesUtil.java, v 0.1 2014年12月29日 下午1:10:59 log.yin Exp $
 */
public class BizResourcesUtil {
    private static Logger       logger     = LoggerFactory.getLogger(BizResourcesUtil.class);

    private static Properties   properties = new Properties();
    private static final String FILE_PATH  = "base.properties";

    public static String        SECURITY_AES_KEY;
    public static String        SECURITY_MD5_KEY;
    public static String        NOTICE_URL;

    //YQ
    public static String        YQ_RECHARGE_URL;
    public static String        YQ_QUERY_URL;
    public static String        YQ_MD5_KEY;

    //NSK
    public static String        NSK_RECHARGE_URL;
    public static String        NSK_RECHARGE_USER;
    public static String        NSK_RECHARGE_PASSWD;

    //Ble mall
    public static String        BleMall_RECHARGE_URL;
    public static String        BleMall_QUERY_URL;
    public static String        BleMall_MD5_KEY;

    public static String        SUPPORTED_FACE_VALUE;
    public static String        QUERY_MOBILE_AREA_URL;

    public static String        DEFAULT_VENDOR_ID;

    static {
        InputStream inputStream = null;
        try {
            String path = System.getProperty("envpath") + File.separator + FILE_PATH;
            inputStream = new FileInputStream(new File(path));

            if (inputStream != null) {
                properties.load(inputStream);

                NOTICE_URL = properties.getProperty("gw.crm.notice.url");
                SECURITY_AES_KEY = properties.getProperty("security.aes.key");
                SECURITY_MD5_KEY = properties.getProperty("security.md5.key");

                YQ_RECHARGE_URL = properties.getProperty("yiqiao.recharge.url");
                YQ_QUERY_URL = properties.getProperty("yiqiao.query.url");
                YQ_MD5_KEY = properties.getProperty("yiqiao.md5.key");

                NSK_RECHARGE_URL = properties.getProperty("nsk.recharge.url");
                NSK_RECHARGE_USER = properties.getProperty("nsk.recharge.user");
                NSK_RECHARGE_PASSWD = properties.getProperty("nsk.recharge.passwd");

                BleMall_RECHARGE_URL = properties.getProperty("blemall.recharge.url");
                BleMall_QUERY_URL = properties.getProperty("blemall.query.url");
                BleMall_MD5_KEY = properties.getProperty("blemall.md5.key");

                SUPPORTED_FACE_VALUE = properties.getProperty("supported.face.value");

                QUERY_MOBILE_AREA_URL = properties.getProperty("query.mobile.area.url");

                DEFAULT_VENDOR_ID = properties.getProperty("default.vendor.id");
            }
        } catch (Exception e) {
            logger.error("Restart your application container", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
