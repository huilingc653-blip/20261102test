package com.bytter.credit.domain.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.core.utils.BeanUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.service.RatItemCustomService;
import com.bytter.credit.domain.service.RatUrationService;
import com.bytter.credit.dto.*;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 授信占用对外接口
 *
 * @author liyh
 */
@Service("ratUrationServiceImpl")
@Transactional
@DubboService
public class RatUrationServiceImpl implements IRatUrationService {
    @Autowired
    private RatUrationService urationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private RatBkrationService ratBkrationService;

    @Autowired
    private RatItemCustomService ratItemCustomService;

    /**
     * 功能介绍 : 授信接口调用前对授信数据进行相关的设置。<br>
     * <b>日期：</b>2017年9月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setRatUrationDTO(RatUrationDTO urationDTO) {
    }
    @Override
    public void tempUseRation(RatUrationDTO urationDTO) {
        urationService.useRation(urationDTO, RationConstant.RAT_USE_STATUS_TEMP);
    }
    @Override
    public void addUseRation(RatUrationDTO urationDTO) {
        urationService.useRation(urationDTO, RationConstant.RAT_USE_STATUS_FREEZE);
    }
    @Override
    public void passUseRation(RatUrationDTO urationDTO) {
        urationService.passUseRation(urationDTO);
    }
    @Override
    public void releaseRation(RatUrationDTO urationDTO) {
        urationDTO.setReleaseFlag(true);// 释放标识
        urationService.releaseRation(urationDTO);
    }
    @Override
    public void backUseRation(RatUrationDTO urationDTO) {
        urationService.backUseRation(urationDTO);
    }
    @Override
    public void preBackUseRation(RatUrationDTO urationDTO) {
        urationService.preBackUseRation(urationDTO);
    }
    @Override
    public RatUrationDTO getRatUrationDTOByBillId(String sysCode, String billId) {
        return urationService.getRatUrationDTOByBillId(sysCode, billId);
    }
    //todo
    @Override
    public String getRatCommonDTOByBillId(String sysCode, String billId) {
        return JSONUtil.toJsonStr(ratBkrationService.getRatCommonDTOByBillId(sysCode, billId));
    }
    @Override
    public List<RatUrationDTO> getRatUrationList(RatUrationDTO urationDTO) {
        return urationService.getRatUrationList(urationDTO);
    }
    @Override
    public String getRatBankCtlConfig() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_BANK_CONTROLTYPE).getValue();// 授信银行控制;
    }
    @Override
    public void submitUseRation(RatUrationDTO urationDTO) {
        urationService.submitUseRation(urationDTO);
    }
    @Override
    public void deleteUseRation(RatUrationDTO urationDTO) {
        urationService.deleteUseRation(urationDTO);
    }
    @Override
    public void directUseRation(RatUrationDTO urationDTO) {
        urationService.useRation(urationDTO, RationConstant.RAT_USE_STATUS_USE);
        passUseRation(urationDTO);
    }
    @Override
    public List<RatUrationDisDTO> viewCorpDisRation(RatUrationDTO urationDTO, String isRmbRat) {
        throw new RuntimeException("暂未实现");
    }
    @Override
    public List<RatItemCustomDTO> getRatCustomByBank(String customId) {
        throw new RuntimeException("暂未实现");
    }
    @Override
    public RatItemCustomDTO getRatCustomByCode(String code) {
        RatItemCustom ratItemCustom = ratItemCustomService.get(code, true);
        return BeanUtils.copyNewInstanceProperties(ratItemCustom, RatItemCustomDTO.class);
    }
    @Override
    public List<String> queryRationBank() {
        return urationService.queryRationBank();
    }
    @Override
    public List<String> queryRationBank(String corpId) {
        return urationService.queryRationBank(corpId);
    }
    @Override
    public List<String> queryRationBill() {
        return urationService.queryRationBill();
    }
    @Override
    public List<GuaContractOccupDTO> getRatGuaWayNameList(List<String> businessIds, String sysCode) {
        return urationService.getRatGuaWayNameList(businessIds,sysCode);
    }
    @Override
    public RatUrationDTO getRatUrationDTOById(String id) {
        throw new RuntimeException("暂未实现");
    }
    /**
     * 功能介绍 : 查询授信额度使用明细。<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
    public List<RatUrationUseVO> queryRationUseDtl(UrationParamDTO paramDTO) {
       List<RatUrationUseVO> listvo = urationService.queryRationUseDtl(paramDTO);
       if(CollectionUtils.isEmpty(listvo)) {
    	   return ListUtil.empty();
       }
       return listvo;
    }


    /**
     * 根据授信编号和授信品种类型获取授信信息
     * @param corpId 授信单位ID
     * @param contractNo 授信合同编号
     * @param itemName 授信品种名称
     * @return 授信信息
     */
    public RatUrationDisDTO getRatUrationDTOByParams(String corpId, String contractNo,String itemName){
        return urationService.getRatUrationDTOByParams(corpId, contractNo, itemName);
    }

    /**
    * @Description 票据指令失败打回
    * @author zhous
    * @date 2025/5/21 17:53
    */
    @Override
    public void billReleaseRation(RatUrationDTO urationDTO) {
        urationService.billReleaseRation(urationDTO);
    }
}
