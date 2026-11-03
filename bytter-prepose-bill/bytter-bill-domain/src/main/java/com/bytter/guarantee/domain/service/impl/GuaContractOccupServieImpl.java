package com.bytter.guarantee.domain.service.impl;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.entity.GuaContractInfo;
import com.bytter.guarantee.domain.entity.GuaContractOccup;
import com.bytter.guarantee.domain.service.GuaContractChangeService;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaContractOccupService;
import com.bytter.guarantee.domain.service.GuaWayService;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import com.bytter.guarantee.dto.GuaContractDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractQueryDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.Ii18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-07-22 18:50
 * @Version 1.0
 */
@Service("guaContractOccupServieImpl")
@DubboService
@Transactional
public class GuaContractOccupServieImpl implements IGuaContractOccupServie {
    @Autowired
    private GuaContractOccupService guaContractOccupService;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Autowired
    private GuaWayService guaWayService;
    @Autowired
    private GuaContractInfoService contractInfoService;
    @Autowired(required = false)
    private GuaContractChangeService contractChangeService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @Override
    public void tempUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO) {
        guaContractOccupService.deleteByBusinessId(guaContractOccupDTO.getBusinessId(),
                guaContractOccupDTO.getSysCode());// 删除之前的担保合同占用信息
        List<GuaContractOccup> guaContractOccupNews = new ArrayList<>();
        for (String contractId : guaContractOccupDTO.getContractIds()) {
            GuaContractOccup guaContractOccup = new GuaContractOccup();
            BeanUtils.copyNotNullProperties(guaContractOccupDTO, guaContractOccup);
            guaContractOccup.setContractId(contractId);
            guaContractOccup.setStatus(GuaranteeConstant.GUA_CONTRACT_TEMP);
            guaContractOccup.setGuaRmk(guaContractOccupDTO.getGuaRmk());
            guaContractOccupNews.add(guaContractOccup);
        }
        guaContractOccupService.batchSave(guaContractOccupNews);
    }
    @Override
    public void submitUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO) {
        guaContractOccupService.deleteByBusinessId(guaContractOccupDTO.getBusinessId(),
                guaContractOccupDTO.getSysCode());// 删除之前的担保合同占用信息
        List<GuaContractOccup> guaContractOccupNews = new ArrayList<>();
        for (String contractId : guaContractOccupDTO.getContractIds()) {
            GuaContractOccup guaContractOccup = new GuaContractOccup();
            BeanUtils.copyNotNullProperties(guaContractOccupDTO, guaContractOccup);
            guaContractOccup.setContractId(contractId);
            guaContractOccup.setStatus(GuaranteeConstant.GUA_CONTRACT_APPROVED);
            guaContractOccup.setGuaRmk(guaContractOccupDTO.getGuaRmk());
            guaContractOccupNews.add(guaContractOccup);
        }
        guaContractOccupService.batchSave(guaContractOccupNews);
    }
    @Override
    public void saveUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO) {
        List<Integer> status = new ArrayList<>();
        status.add(GuaranteeConstant.GUA_CONTRACT_APPROVED);
        Map<String, Object> params = new HashMap<>();
        params.put("businessId", guaContractOccupDTO.getBusinessId());
        params.put("contractIds", guaContractOccupDTO.getContractIds());
        params.put("status", status);
        guaContractOccupService.updateGuaSatus(params, GuaranteeConstant.GUA_CONTRACT_USED);
    }
    @Override
    public void releaseUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO) {
        backOrReleaseGuaContractOccup(guaContractOccupDTO, GuaranteeConstant.GUA_CONTRACT_RELEASE);
    }
    @Override
    public void backUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO) {
        backOrReleaseGuaContractOccup(guaContractOccupDTO, GuaranteeConstant.GUA_CONTRACT_TEMP);
    }
    @Override
    public void sendBackUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO) {
        backOrReleaseGuaContractOccup(guaContractOccupDTO, GuaranteeConstant.GUA_CONTRACT_APPROVED);// 发放打回设置担保合同状态为在审使用
    }
    @Override
    public GuaContractOccupDTO getUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO) {
        List<Integer> status = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("businessId", guaContractOccupDTO.getBusinessId());
        params.put("contractIds", guaContractOccupDTO.getContractIds());
        status.add(GuaranteeConstant.GUA_CONTRACT_TEMP);
        status.add(GuaranteeConstant.GUA_CONTRACT_APPROVED);
        status.add(GuaranteeConstant.GUA_CONTRACT_USED);
        status.add(GuaranteeConstant.GUA_CONTRACT_RELEASE);
        params.put("status", status);
        GuaContractOccupDTO guaContractOccupDTOS = new GuaContractOccupDTO();
        List<GuaContractOccup> guaContractOccups = guaContractOccupService
                .getGuaContractOccupByParams(guaContractOccupDTO, params);
        List<String> contractIds = new ArrayList<>();
        if (null != guaContractOccups && guaContractOccups.size() > 0) {
            GuaContractOccupDTO guaContractOccupDTONew = new GuaContractOccupDTO();
            String contractCode = null;
            BeanUtils.copyNotNullProperties(guaContractOccups.get(0), guaContractOccupDTONew);// 取数据的第一条
            for (GuaContractOccup guaContractOccup : guaContractOccups) {
                contractIds.add(guaContractOccup.getContractId());
                GuaContractInfo guaContractInfo = contractInfoService.get(guaContractOccup.getContractId());
                if (StringUtils.isEmpty(contractCode)) {
                    contractCode = guaContractInfo.getContractCode();
                } else {
                    contractCode += "," + guaContractInfo.getContractCode();
                }
            }
            guaContractOccupDTOS.setContractCode(contractCode);
            guaContractOccupDTOS.setContractIds(contractIds);
            if (StringUtils.isNotEmpty(guaContractOccupDTO.getGuaWay())) {
                guaContractOccupDTOS.setGuaWayName(guaWayServie.getGuaWayNameByCodes(guaContractOccupDTO.getGuaWay()));
            } else {
                String GuaWayName = null;
                String guaWay = null;
                params.put("contractIds", contractIds);
                List<GuaWayVO> guaWayList = guaWayService.queryGuaWayByConstractIds(params);
                if (guaWayList != null && guaWayList.size() > 0) {
                    for (int i = 0; i < guaWayList.size(); i++) {
                        GuaWayVO guaWayObj = guaWayList.get(i);
                        if (i == 0) {
                            GuaWayName = guaWayObj.getName();
                            guaWay = guaWayObj.getCode();
                        } else {
                            GuaWayName += "," + guaWayObj.getName();
                            guaWay += "," + guaWayObj.getCode();
                        }
                    }
                }
                // 如果担保方式id为空，则根据担保合同查询担保方式
                guaContractOccupDTOS.setGuaWayName(GuaWayName);
                guaContractOccupDTOS.setGuaWay(guaWay);
            }
            guaContractOccupDTOS.setGuaRmk(guaContractOccupDTONew.getGuaRmk());
        }
        return guaContractOccupDTOS;
    }
    @Override
    public void deleteGuaContractOccup(String businessId, String sysCode) {
        guaContractOccupService.deleteByBusinessId(businessId, sysCode);// 根据业务id和业务类型删除之前的担保合同占用信息
    }
    @Override
    public GuaContractUseDTO getGuaContractOccupyDtl(String businessId, String itemNo) {
        List<Integer> status = new ArrayList<>();
        GuaContractUseDTO guaContractUseDTO = new GuaContractUseDTO();
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        guaContractOccupDTO.setBusinessId(businessId);
        Map<String, Object> params = new HashMap<>();
        params.put("businessId", businessId);
        status.add(GuaranteeConstant.GUA_CONTRACT_TEMP);
        status.add(GuaranteeConstant.GUA_CONTRACT_APPROVED);
        status.add(GuaranteeConstant.GUA_CONTRACT_USED);
        status.add(GuaranteeConstant.GUA_CONTRACT_RELEASE);
        params.put("status", status);
        List<GuaContractOccup> guaContractOccups = guaContractOccupService
                .getGuaContractOccupByParams(guaContractOccupDTO, params);
        if (null != guaContractOccups && guaContractOccups.size() > 0) {
            GuaContractOccup guaContractOccup = guaContractOccups.get(0);
            BeanUtils.copyNotNullProperties(guaContractOccup, guaContractUseDTO);
            List<GuaContractDTO> guaContractDTOS = guaContractOccupService.getGuaContractOccupyDtl(businessId, itemNo);
            guaContractUseDTO.setContracts(guaContractDTOS);
        }
        return guaContractUseDTO;
    }
    /**
     * 打回或者释放
     *
     * @param guaContractOccupDTO
     * @param backOrRelease
     */
    private void backOrReleaseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO, Integer backOrRelease) {
        List<Integer> status = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("businessId", guaContractOccupDTO.getBusinessId());
        params.put("contractIds", guaContractOccupDTO.getContractIds());
        status.add(GuaranteeConstant.GUA_CONTRACT_TEMP);
        status.add(GuaranteeConstant.GUA_CONTRACT_APPROVED);
        status.add(GuaranteeConstant.GUA_CONTRACT_USED);
        params.put("status", status);
        guaContractOccupService.updateGuaSatus(params, backOrRelease);
    }
    /**
     * 根据主业务id和业务编码查询担保方式
     *
     * @param businessIds
     * @param sysCode
     * @return
     */
    @Override
    public List<GuaContractQueryDTO> listGuaWayName(List<String> businessIds, String sysCode) {
        return contractChangeService.listGuaWayName(businessIds, sysCode);
    }

    /**
     * 获取挂接担保的业务id
     *
     * @param sysCode   查询业务种类
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param status    担保状态
     * @return 业务id集合，
     */
    @Override
    public List<GuaContractOccupDTO> getGua(String sysCode, String startDate, String endDate, String status,String contractId) {
        return contractChangeService.getGua(sysCode, startDate, endDate, status,contractId);
    }

    @Override
    public List<GuaContractOccupDTO> listUseGua(String sysCode, String startDate, String endDate, String status) {
        return guaContractOccupService.listUseGua(sysCode, startDate, endDate, status);
    }
}
