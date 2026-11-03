package com.bytter.guarantee.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import org.apache.velocity.tools.config.DefaultKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaPledgeTag
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/7/13 11:00
 */
@DefaultKey("guaPledgeTag")
public class GuaPledgeTag {
    private final IGuaPledgeOccupyService guaPledgeOccupyServiceImpl;
    public GuaPledgeTag() {
        this.guaPledgeOccupyServiceImpl = ApplicationContextUtil.getContext().getBean("guaPledgeOccupyServiceI", IGuaPledgeOccupyService.class);
    }
    public List<GuaPledgeOccupyDTO> getPledgeOccupy(String businessId, String itemNo){
        return guaPledgeOccupyServiceImpl.getPledgeOccupy(businessId,itemNo);
    }
}
