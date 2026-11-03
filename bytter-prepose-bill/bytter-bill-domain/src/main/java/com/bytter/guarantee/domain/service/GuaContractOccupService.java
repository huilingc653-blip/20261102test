package com.bytter.guarantee.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaContractInfoDao;
import com.bytter.guarantee.domain.dao.GuaContractOccupDao;
import com.bytter.guarantee.domain.entity.GuaContractOccup;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import com.bytter.guarantee.dto.GuaContractDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.Ii18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @Title: GuaContractOccupService
 * @ProjectName T2_IDEA
 * @Description: 担保合同业务逻辑层
 * @date 2019/4/30 19:48
 */
@Service("GuaContractOccupService")
@Transactional
public class GuaContractOccupService extends BaseService<GuaContractOccup, String> {
    @Autowired
    private GuaContractOccupDao guaContractOccupDao;
    private ICorpService corpService;
    @Autowired
    private GuaContractInfoDao guaContractInfoDao;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaContractWayService getGuaContractWay;
    @DubboReference(check = false)
    private Ii18nService ii18nService;

    /**
     * 根据业务id和状态查询担保合同占用信息
     */
    public List<GuaContractOccup> queryByBusinessId(String businessId, Integer status) {
        return guaContractOccupDao.queryGuaContractByBusinessId(businessId, status);
    }
    /**
     * 根据业务id和担保合同id查询担保合同占用信息
     */
    public GuaContractOccup queryByParams(String businessId, String contractId, Integer status) {
        return guaContractOccupDao.queryGuaContractByParams(businessId, contractId, status);
    }
    /**
     * 根据业务id删除担保合同占用信息
     *
     * @return
     */
    public void deleteByBusinessId(String businessId, String sysCode) {
        guaContractOccupDao.deleteGuaContractByBusinessId(businessId, sysCode);
    }
    /**
     * 根据id删除担保合同占用信息
     *
     * @return
     */
    public void deleteById(String id) {
        this.deleteById(id);
        this.getSession().flush();
    }
    /**
     * 根据id修改担保合同占用状态
     *
     * @return
     */
    public void changeStatusById(String id, Integer status) {
        GuaContractOccup guaContractOccup = this.get(id);
        guaContractOccup.setStatus(status);
        this.saveOrUpdate(guaContractOccup);
    }

    /**
     * 按照状态查找
     *
     * @param guaContractOccupDTO
     * @param params
     * @return
     */
    public List<GuaContractOccup> getGuaContractOccupByParams(GuaContractOccupDTO guaContractOccupDTO,
            Map<String, Object> params) {
        return guaContractOccupDao.queryGuaContractByParams(params);
    }
    /**
     * 更新状态
     *
     * @param params
     * @param status
     */
    public void updateGuaSatus(Map<String, Object> params, Integer status) {
        guaContractOccupDao.updateGuaStatus(params, status);
    }
    @Override
    public BaseDao<GuaContractOccup, String> getEntityDao() {
        return guaContractOccupDao;
    }

    @Override
    public void batchSaveOrUpdate(List<GuaContractOccup> entityList) {
        super.batchSaveOrUpdate(entityList);
    }
    public List<GuaContractDTO> getGuaContractOccupyDtl(String businessId, String itemNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("businessId", businessId);
        if (StringUtils.isNotEmpty(itemNo)) {
            params.put("itemNo", itemNo);
        }
        List<GuaContractDTO> guaContractOccupDTOS = guaContractOccupDao.getGuaContractOccup(params);
        GuaWayVO guaWayVO = null;
        for (GuaContractDTO guaContractOccupDTO : guaContractOccupDTOS) {
            List<GuaContractWayVO> guaContractWays = getGuaContractWay.getGuaContractWay(guaContractOccupDTO.getId());
            for (GuaContractWayVO guaContractWay : guaContractWays) {
                guaWayVO = guaContractInfoDao.getGuaWayById(guaContractWay.getWayCodes());
                if (StringUtils.isEmpty(guaContractOccupDTO.getGuaTypeName())) {
                    guaContractOccupDTO.setGuaTypeName(guaWayVO.getName());
                    guaContractOccupDTO.setGuaWayCode(guaWayVO.getCode());
                } else {
                    guaContractOccupDTO.setGuaTypeName(guaContractOccupDTO.getGuaTypeName() + "," + guaWayVO.getName());
                    guaContractOccupDTO.setGuaWayCode(guaContractOccupDTO.getGuaWayCode() + "," + guaWayVO.getCode());
                }
            }
        }
        return guaContractOccupDTOS;
    }

    public List<GuaContractOccupDTO> listUseGua(String sysCode, String startDate, String endDate, String status) {
        return guaContractOccupDao.listUseGua(sysCode, startDate, endDate, status);
    }

    public DataTablesOutput getUseContractList(DataTablesInput input, ICurrentUser user){
      DataTablesOutput dataTablesOutput = null;
      String eqs_sysCode = input.getFormdata().get("eqs_sysCode").toString();
      switch (eqs_sysCode) {
          case "accebill"://票据
              dataTablesOutput = guaContractOccupDao.getGuaForbill(input,null);
              break;
          case "provide"://银行借款
              dataTablesOutput = guaContractOccupDao.getGuaForCmsProvideLoan(input,null);
              break;
          case "loan"://内部贷款
              dataTablesOutput = guaContractOccupDao.getGuaForloan(input,null);
              break;
          case "lcs"://国际结算
              dataTablesOutput = guaContractOccupDao.getGuaForInternational(input,null);
              break;
          case "inl"://内部拆借
              dataTablesOutput = guaContractOccupDao.getGuaForInl(input,null);
              break;
          case "cmsBond"://债券
              dataTablesOutput = guaContractOccupDao.getGuaForCmsBond(input,null);
              break;
          case "lease"://融资租赁
              dataTablesOutput = guaContractOccupDao.getGuaForLease(input,null);
              break;
          case "security"://资产证券化
              dataTablesOutput = guaContractOccupDao.getGuaForSecurity(input,null);
              break;
           default://默认授信
               dataTablesOutput = guaContractOccupDao.getUseContractList(input,null);
      }
      return dataTablesOutput;
  }

    public GuaContractUseDTO getGuaContractOccupyDtlDTO(String businessId, String itemNo) {
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
        List<GuaContractOccup> guaContractOccups = this.getGuaContractOccupByParams(guaContractOccupDTO, params);
        if (null != guaContractOccups && guaContractOccups.size() > 0) {
            GuaContractOccup guaContractOccup = guaContractOccups.get(0);
            BeanUtils.copyNotNullProperties(guaContractOccup, guaContractUseDTO);
            List<GuaContractDTO> guaContractDTOS = this.getGuaContractOccupyDtl(businessId, itemNo);
            guaContractUseDTO.setContracts(guaContractDTOS);
        }
        return guaContractUseDTO;
    }

}
