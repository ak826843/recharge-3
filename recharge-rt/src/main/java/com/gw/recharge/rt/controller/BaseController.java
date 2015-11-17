package com.gw.recharge.rt.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gw.recharge.dal.model.ClientDO;
import com.gw.recharge.rt.service.ClientService;
import com.gw.steel.steel.web.constants.BaseConstants;
import com.gw.steel.steel.web.controller.GenericController;
import com.gw.steel.steel.web.controller.dto.BaseRequest;
import com.gw.steel.steel.web.controller.dto.BaseResponse;

public abstract class BaseController<Req extends BaseRequest, Resp extends BaseResponse>
		extends GenericController<Req, Resp> {

	@Autowired
	private ClientService clientService;

	@Override
	public Map<String, String> getExtendsDataMap(Req req) {
		Map<String, String> dataMap = new HashMap<String, String>();
		ClientDO clientDO = clientService.selectByClientNo(req
				.getClientNo());
		if (clientDO != null && StringUtils.isNotBlank(clientDO.getClientKey())) {
			dataMap.put(BaseConstants.KEY, clientDO.getClientKey().trim());
		}
		dataMap.put(BaseConstants.SUPPORT_VERSION_KEY, BaseConstants.DEFAULT_VERSION);
		dataMap.put(BaseConstants.SUPPORT_SIGN_TYPE_KEY, BaseConstants.DEFAULT_SIGN_TYPE);
		dataMap.put(BaseConstants.SUPPORT_INPUT_CHARSET_KEY, BaseConstants.DEFAULT_INPUT_CHARSET);
		return dataMap;
	}

}
