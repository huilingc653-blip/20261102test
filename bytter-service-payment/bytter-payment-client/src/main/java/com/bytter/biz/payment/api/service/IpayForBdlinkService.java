package com.bytter.biz.payment.api.service;

import com.bytter.core.common.model.json.AjaxJson;

public interface IpayForBdlinkService {
	
	AjaxJson bdlinkToPayTrade(String cbsBillId,String busiType,String toUpdateStatus,String voucvherFrom);
}
