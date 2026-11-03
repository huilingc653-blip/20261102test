package com.bytter.biz.trade.service;

import com.bytter.core.common.model.json.AjaxJson;

public interface BdlinkToPayService {
	
	AjaxJson bdlinkToPayTrade(String voucher_no,String busiType,String toUpdateStatus);
	
}
