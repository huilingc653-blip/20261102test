package com.bytter.bill.velocity.tags;

import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.JsonUtils;
import com.bytter.credit.api.IRatUrationService;
import org.apache.velocity.tools.config.DefaultKey;

import java.util.Map;

@DefaultKey("ratUrationTag")
public class RatUrationTag {
    private final IRatUrationService iRatUrationService;
    public RatUrationTag() {
        this.iRatUrationService = ApplicationContextUtil.getContext().getBean("ratUrationServiceImpl", IRatUrationService.class);
    }
    public Map getRatCommonDTOByBillId(String sysCode, String billId) {
        String str = iRatUrationService.getRatCommonDTOByBillId(sysCode, billId);
        return JsonUtils.json2Map(str);
    }
}
