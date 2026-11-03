package com.bytter.biz.receipt.service;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.payment.api.service.IEVisaTypeService;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.utils.EmptyUtils;
/**
 * add by qijh
 */
@Service("eVisaTypeServiceImpl")
@Transactional
@DubboService
public class EVisaTypeServiceImpl implements IEVisaTypeService {
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @Override
    public String getAttachmentId(String billType, String netId) {
        String atId = null;
        EVisaTypeVO vo = eVisaTypeService.getVOByParamsAndValid(billType,netId);
        if(!EmptyUtils.isEmpty(vo)){
            atId = vo.getFileName();
        }
        return atId;
    }
}
