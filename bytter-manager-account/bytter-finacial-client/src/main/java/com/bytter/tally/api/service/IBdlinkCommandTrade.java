package com.bytter.tally.api.service;

import java.util.Map;

/**
 * 公共交易？
 */
public interface IBdlinkCommandTrade {
	
	Map<String,String> ibdlinkTrade(String voucherNo,String busiType);
	
	int updateTradeStatusForCommand(String voucherNo, String busiType, String toUpdateStatus);
}
