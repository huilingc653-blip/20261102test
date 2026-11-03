package com.bytter.guarantee.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import org.apache.velocity.tools.config.DefaultKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
/**
 * @author luxy
 * @Title: GuaContractTag
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/7/23 14:23
 */
@DefaultKey("guaContractTag")
public class GuaContractTag {
    private final IGuaContractOccupServie guaContractOccupServiceImpl;
    public GuaContractTag() {
        this.guaContractOccupServiceImpl = ApplicationContextUtil.getContext().getBean("guaContractOccupServiceI", IGuaContractOccupServie.class);
    }
    public GuaContractUseDTO getGuaContractOccupyDetail(String businessId, String itemNo){
        return guaContractOccupServiceImpl.getGuaContractOccupyDtl(businessId,itemNo);
    }
}
