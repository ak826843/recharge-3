/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.check;

import java.text.MessageFormat;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.gw.recharge.rt.util.CodeConstants;
import com.gw.steel.steel.util.common.CodeResourcesUtil;

/**
 * 
 * @author log.yin
 * @version $Id: CheckCode00001.java, v 0.1 2015年2月3日 下午2:19:50 log.yin Exp $
 */
public class CheckCode10001 implements Command {
    /** 
     * @see org.apache.commons.chain.Command#execute(org.apache.commons.chain.Context)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(Context context) throws Exception {
        Object mobile = context.get("mobile");

        if (mobile == null) {
            context.put("code", CodeConstants.INVALID_PARAM);
            context.put("message", MessageFormat.format(
                CodeResourcesUtil.getProperty(CodeConstants.INVALID_PARAM), "mobile为空"));
            return PROCESSING_COMPLETE;
        }

        int length = mobile.toString().length();
        if (length != 11) {
            context.put("code", CodeConstants.INVALID_MOBILE);
            return PROCESSING_COMPLETE;
        }

        String firstNum = mobile.toString().charAt(0) + "";
        if (!"1".equals(firstNum)) {
            context.put("code", CodeConstants.INVALID_MOBILE);
            return PROCESSING_COMPLETE;
        }

        for (int i = 1; i < length; i++) {
            try {
                int number = Integer.parseInt(mobile.toString().charAt(i) + "");
                if (number < 0 || number > 9) {
                    context.put("code", CodeConstants.INVALID_MOBILE);
                    return PROCESSING_COMPLETE;
                }
            } catch (Exception e) {
                context.put("code", CodeConstants.INVALID_MOBILE);
                return PROCESSING_COMPLETE;
            }
        }

        return CONTINUE_PROCESSING;
    }
}
