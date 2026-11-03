package com.bytter.tally.service.impl;
import com.bytter.core.utils.BeanUtils;
import com.bytter.tally.api.dto.FcUnionAccbookDTO;
import com.bytter.tally.api.service.IFcUnionAccbookService;
import com.bytter.tally.service.FcUnionAccbookService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 往来信息对外接口
 */
@Service("fcUnionAccbookServiceImpl")
@Transactional
@DubboService
public class FcUnionAccbookServiceImpl implements IFcUnionAccbookService {
    
    @Autowired
    private FcUnionAccbookService unionAccbookService;
    @Override
    public void saveFcUnionAccbook(FcUnionAccbookDTO accbookDTO) {
        com.bytter.biz.finacial.api.dto.FcUnionAccbookDTO dto = new com.bytter.biz.finacial.api.dto.FcUnionAccbookDTO();
        BeanUtils.copyProperties(accbookDTO,dto);
        unionAccbookService.saveFcUnionAccbook(dto);
    }
    @Override
    public List<FcUnionAccbookDTO> getUnionAccbookBySendNo(String sendVoucherNo) {
        List<com.bytter.biz.finacial.api.dto.FcUnionAccbookDTO> dtos = unionAccbookService.getUnionAccbookBySendNo(sendVoucherNo);
        return BeanUtils.copyListProperties(dtos,FcUnionAccbookDTO.class);
    }
    /**
     * 更新往来信息状态
     * @param id
     * @param billState
     * @param oldBillState
     */
    @Override
    public void updateUnionAccbookBillState(String id, String billState, String oldBillState) {
        unionAccbookService.updateUnionAccbookBillState(id, billState, oldBillState);
    }
}
