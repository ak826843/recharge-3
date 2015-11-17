/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.integration.dto.yq;

/**
 * 
 * @author log.yin
 * @version $Id: YQParamsConstants.java, v 0.1 2015年2月3日 上午10:38:20 log.yin Exp $
 */
public class YQConstants {
    //recharge request
    public static final String Code                = "Code";
    public static final String OrderID             = "OrderID";
    public static final String CardID              = "CardID";
    public static final String Account             = "Account";
    public static final String Amount              = "Amount";
    public static final String NotifyUrl           = "Notifyurl";
    public static final String Sign                = "Sign";
    //recharge response
    //    public static final String OrderID      = "OrderID";
    public static final String OrderStatus         = "OrderStatus";
    public static final String failCode            = "failCode";
    public static final String failDesc            = "failDesc";

    //query request
    /*public static final String Code      = "Code";
    public static final String OrderID   = "OrderID";
    public static final String CardID    = "CardID";
    public static final String Sign      = "Sign";*/
    public static final String key                = "key";
    //query response
    /*  public static final String OrderID      = "OrderID";
      public static final String OrderStatus   = "OrderStatus";
      public static final String failCode    = "failCode";
      public static final String failDesc   = "failDesc";*/

    //order notice
    public static final String MerchantId          = "MerchantId";
    public static final String MerchantOrderId     = "MerchantOrderId";
    public static final String MerchantOrderStatus = "MerchantOrderStatus";
    public static final String YqOrderNo           = "YqOrderNo";
    public static final String SuccessTime         = "SuccessTime";

   // 意桥支持的业务类型： 充值
 	public static final String RECHARGE_CARD_ID = "100";
 	
 	//error code
 	public static final String FAIL_CODE_UNSUPPORTED_MOBILE = "205";
}
