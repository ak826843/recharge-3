/**
 * 
 *大智慧股份有限公司
 * Copyright (c) 2006-2015 DZH,Inc.All Rights Reserved.
 */
package com.gw.recharge.rt.check;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gw.recharge.rt.type.BizType;
import com.gw.recharge.rt.util.BizResourcesUtil;
import com.gw.recharge.rt.util.CodeConstants;
import com.gw.steel.steel.util.common.CodeResourcesUtil;

/**
 * 
 * @author log.yin
 * @version $Id: CheckCode00001.java, v 0.1 2015年2月3日 下午2:19:50 log.yin Exp $
 */
public class CheckCode10002 implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CheckCode10002.class);

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(Context context) throws Exception {
        Object bizType = context.get("bizType");
        Object amount = context.get("amount");

        if (bizType == null || bizType.toString().trim().length() == 0) {
            context.put("code", CodeConstants.INVALID_PARAM);
            context.put("message", MessageFormat.format(
                CodeResourcesUtil.getProperty(CodeConstants.INVALID_PARAM), "bizType为空"));
            return PROCESSING_COMPLETE;
        }
        if (amount == null || amount.toString().trim().length() == 0) {
            context.put("code", CodeConstants.INVALID_PARAM);
            context.put("message", MessageFormat.format(
                CodeResourcesUtil.getProperty(CodeConstants.INVALID_PARAM), "amount为空"));
            return PROCESSING_COMPLETE;
        }
        if (!(BizType.RECHARGE.getCode() + "").equals(bizType.toString())) {
            context.put("code", CodeConstants.INVALID_PARAM);
            context.put("message", MessageFormat.format(
                CodeResourcesUtil.getProperty(CodeConstants.INVALID_PARAM), "bizType不支持"));
            return PROCESSING_COMPLETE;
        }

        Set<String> faceValues = new HashSet<String>();
        String[] supportedFaceValue = BizResourcesUtil.SUPPORTED_FACE_VALUE.split(",");
        for (String faceValue : supportedFaceValue) {
            faceValues.add(faceValue);
        }

        if (!faceValues.contains(amount.toString())) {
            context.put("code", CodeConstants.NOT_SUPPORTED_FACE_VALUE);
            logger.info("不支持的面值：{}", amount.toString());
            return PROCESSING_COMPLETE;
        }
        return CONTINUE_PROCESSING;
    }

}
