package com.bytter.guarantee.velocity.tags;

import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import org.apache.velocity.tools.config.DefaultKey;

/**
 * 保证金标签实现类。
 *
 * @author zhaohc
 * @date 2023/8/29 10:53
 */
@DefaultKey("fcBondTag")
public class FcBondTag {

    private final IFcBondHookService fcBondHookServiceI;

    public FcBondTag() {
        this.fcBondHookServiceI = ApplicationContextUtil.getContext().getBean("fcBondHookServiceI", IFcBondHookService.class);;
    }

    public FcBondApplyDTO getBondApplyDetail(String itemId, String itemNo){
        return fcBondHookServiceI.getFcBondApplyByItemIds(itemId,itemNo);
    }

    public FcBondApplyDTO getBondUseDetail(String itemId,String itemNo){
        FcBondApplyDTO fcBondApplyDTO=new FcBondApplyDTO();
        fcBondApplyDTO.setItemNo(itemNo);
        fcBondApplyDTO.setItemId(itemId);
        return fcBondHookServiceI.getFcBondByParams(fcBondApplyDTO);
    }
}
