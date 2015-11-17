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
import com.gw.steel.steel.util.common.DateUtils;

/**
 * 
 * @author log.yin
 * @version $Id: CheckCode00001.java, v 0.1 2015年2月3日 下午2:19:50 log.yin Exp $
 */
public class CheckCode00003 implements Command {
    /** 
     * @see org.apache.commons.chain.Command#execute(org.apache.commons.chain.Context)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(Context context) throws Exception {
        Object operaterTime = context.get("operaterTime");

        if (operaterTime == null) {
            context.put("code", CodeConstants.INVALID_PARAM);
            context.put("message", MessageFormat.format(
                CodeResourcesUtil.getProperty(CodeConstants.INVALID_PARAM), "operaterTime为空"));
            return PROCESSING_COMPLETE;
        }
        try {
            DateUtils.parse(operaterTime.toString(), "yyyyMMddHHmmss");
        } catch (Exception e) {
            context.put("code", CodeConstants.INVALID_PARAM);
            context.put("message", MessageFormat.format(
                CodeResourcesUtil.getProperty(CodeConstants.INVALID_PARAM), "operaterTime格式错误"));
            return PROCESSING_COMPLETE;
        }

        return CONTINUE_PROCESSING;
    }

}
