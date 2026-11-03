package com.bytter.increment.domain.service.impl;
import com.bytter.core.utils.BeanUtils;
import com.bytter.increment.api.dto.FcAgreedDepositDTO;
import com.bytter.increment.api.service.IFcAgreedDepositService;
import com.bytter.increment.domain.service.ChoInnerAgreedDepositService;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
/**
 * 协定存款对外接口实现类
 * @author wangzx
 *
 */
@Service("choInnerAgreedDepositServiceImpl")
@Transactional
@DubboService
public class ChoInnerAgreedDepositServiceImpl implements IFcAgreedDepositService {
    @Autowired
    private ChoInnerAgreedDepositService choInnerAgreedDepositService;
    /**
     * 根据协定存款账号和日期查找登记信息
     *
     * @param blnctrAcc 协定主表账号
     * @return 协定存款DTO
     */
    @Override
    public FcAgreedDepositDTO getFcAgreedDepositByBlnctrAcc(String blnctrAcc,Date queryDate) {
        FcAgreedDepositVO vo = choInnerAgreedDepositService.getChoAgreeInfoByAccout(blnctrAcc,queryDate);
        FcAgreedDepositDTO dto = null;
        if(vo != null){
            dto = new FcAgreedDepositDTO();
            BeanUtils.copyNotNullProperties(vo,dto);
        }
        return dto;
    }
    /**
     * 根据协定存款协定账号和日期查找登记信息
     *
     * @param agrAcc 协定账号
     * @return 协定存款DTO
     */
    @Override
    public FcAgreedDepositDTO getChoAgreeInfoByAgrAcc(String agrAcc,Date queryDate) {
        FcAgreedDepositVO vo = choInnerAgreedDepositService.getChoAgreeInfoByAgrAcc(agrAcc,queryDate);
        FcAgreedDepositDTO dto = null;
        if(vo != null){
            dto = new FcAgreedDepositDTO();
            BeanUtils.copyNotNullProperties(vo,dto);
        }
        return dto;
    }
}
