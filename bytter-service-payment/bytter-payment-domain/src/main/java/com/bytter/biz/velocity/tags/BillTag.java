package com.bytter.biz.velocity.tags;
import com.bytter.bill.api.bill.dto.AbVoucherinfoViceDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.velocity.tools.config.DefaultKey;

import java.util.List;

@DefaultKey("billTag")
public class BillTag {
    private IAccebillService accebillService;

    public BillTag() {
        this.accebillService = ApplicationContextUtil.getContext().getBean("accebillServiceI", IAccebillService.class);
    }
    /**
     * 通过票据IDS查询票据具体信息
     * @param aids
     * @return list信息
     */
    public List<AbVoucherinfoViceDTO> listAccebillInfoByIds(String aids, String payId){
        List<AbVoucherinfoViceDTO> list=accebillService.listAccebillInfoByIds(aids, payId);
        return list;
    }
}
