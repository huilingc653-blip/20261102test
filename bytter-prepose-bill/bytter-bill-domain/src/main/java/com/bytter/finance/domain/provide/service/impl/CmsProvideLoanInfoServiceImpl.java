package com.bytter.finance.domain.provide.service.impl;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.dto.CmsProvideLoanInfoDTO;
import com.bytter.finance.api.ICmsProvideLoanInfoService;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service("cmsProvideLoanInfoServiceImpl")
@Transactional
@DubboService
public class CmsProvideLoanInfoServiceImpl implements ICmsProvideLoanInfoService {
    @Autowired
    private LoanInfoService loanInfoService;
    @Override
    public CmsProvideLoanInfoDTO getInfoById(String id) {
        LoanInfoVO loanInfoVO = loanInfoService.getLoanInfoVOById(id);
        CmsProvideLoanInfoDTO cmsProvideLoanInfoDTO = new CmsProvideLoanInfoDTO();
        BeanUtils.copyProperties(loanInfoVO, cmsProvideLoanInfoDTO);
        return cmsProvideLoanInfoDTO;
    }
    @Override
    public List<String> getIdListByRat(){
        return loanInfoService.getIdListByRat();
    }

    @Override
    public CmsProvideLoanInfoDTO getInfoByBillCode(String sendBillCode) {
        LoanInfoVO loanInfoVO = loanInfoService.getLoanInfoVOBySendBillCode(sendBillCode);
        CmsProvideLoanInfoDTO cmsProvideLoanInfoDTO = new CmsProvideLoanInfoDTO();
        BeanUtils.copyProperties(loanInfoVO, cmsProvideLoanInfoDTO);
        return cmsProvideLoanInfoDTO;
    }
}
