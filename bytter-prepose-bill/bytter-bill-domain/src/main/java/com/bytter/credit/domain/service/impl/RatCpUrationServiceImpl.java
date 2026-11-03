package com.bytter.credit.domain.service.impl;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.service.RatCpUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCpUrationServiceImpl
 * @Description:企业授信挂接实现类
 * @date 2018年12月5日 上午9:21:03
 */
@Service("RatCpUrationServiceImpl")
@Transactional
@DubboService
public class RatCpUrationServiceImpl implements IRatCpUrationService {
    @Autowired
    private RatCpUrationService ratCpUService;
    @Override
    public void tempUseCpRation(RatUrationDTO urationDTO) {
        ratCpUService.useCpRation(urationDTO, RationConstant.RAT_USE_STATUS_TEMP);
    }
    @Override
    public void addUseCpRation(RatUrationDTO urationDTO) {
        ratCpUService.useCpRation(urationDTO, RationConstant.RAT_USE_STATUS_FREEZE);
    }
    @Override
    public void deleteUseCpRation(RatUrationDTO urationDTO) {
        ratCpUService.deleteUseRation(urationDTO);
    }
    @Override
    public void backUseCpRation(RatUrationDTO urationDTO) {
        ratCpUService.backUseCpRation(urationDTO);
    }
    @Override
    public void passUseCpRation(RatUrationDTO urationDTO) {
        ratCpUService.passUseRation(urationDTO);
    }
    @Override
    public RatUrationDTO getRatUrationDTOByBillId(String sysCode, String billId) {
        return ratCpUService.getRatUrationDTOByBillId(sysCode, billId);
    }
    @Override
    public void submitUseRation(RatUrationDTO urationDTO) {
        ratCpUService.submitUseCpRation(urationDTO);
    }
    @Override
    public void releaseRation(RatUrationDTO urationDTO) {
        urationDTO.setReleaseFlag(true);// 释放标识
        ratCpUService.releaseRation(urationDTO);
    }
    @Override
    public void preBackUseRation(RatUrationDTO urationDTO) {
        ratCpUService.preBackUseRation(urationDTO);
    }
}
