package com.bytter.guarantee.domain.service.impl;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.service.GuaPledgeOccupyService;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyVO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaPledgeOccupyServiceImpl
 * @ProjectName T3(3.2)
 * @Description: 抵质押物占用实现层
 * @date 2020/7/7 11:27
 */
@Service("guaPledgeOccupyServiceImpl")
@DubboService
@Transactional
public class GuaPledgeOccupyServiceImpl implements IGuaPledgeOccupyService {
    @Autowired
    private GuaPledgeOccupyService pledgeOccupyService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Override
    public void addPledgeOccupy(List<GuaPledgeOccupyDTO> list) {
        if (null != list && list.size() > 0) {
            pledgeOccupyService.savePledgeOccupy(list, GuaranteeConstant.GUA_PLEDGE_OCCUPY, list.get(0).getSysCode());
        }
    }
    @Override
    public void backPledgeOccupy(String businessId, String itemNo) {
        pledgeOccupyService.backPledgeOccupy(businessId, itemNo);
    }
    @Override
    public void freezePledgeOccupy(List<GuaPledgeOccupyDTO> list) {
        if (!EmptyUtils.isEmptyList(list) && list.size() > 0) {
            pledgeOccupyService.deletePledgeOccupy(list.get(0).getBusinessId(), list.get(0).getItemNo());// 将原占用信息删除
            pledgeOccupyService.savePledgeOccupy(list, GuaranteeConstant.GUA_PLEDGE_FROZEN, list.get(0).getSysCode());
            pledgeOccupyService.getEntityDao().flush();
        }
    }
    @Override
    public void savePledgeOccupy(List<GuaPledgeOccupyDTO> list, Integer status, String sysCode) {
        pledgeOccupyService.savePledgeOccupy(list, status, sysCode);
    }
    @Override
    public void deletePledgeOccupy(String businessId, String itemNo) {
        pledgeOccupyService.deletePledgeOccupy(businessId, itemNo);
    }
    @Override
    public void updatePledgeOccupy(String businessId, String pledgeId) {
        pledgeOccupyService.updatePledgeOccupy(businessId, pledgeId);
    }
    @Override
    public void releasePledgeOccupy(String businessId, String itemNo) {
        pledgeOccupyService.releasePledgeOccupy(businessId, itemNo);
    }
    @Override
    public List<GuaPledgeOccupyDTO> getPledgeOccupy(String businessId, String itemNo) {
        List<GuaPledgeOccupyDTO> GuaPledgeOccupyDTO = pledgeOccupyService.getOccupyList(businessId, itemNo);
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        if (CommonConstant.YES.equals(configDTO.getValue())) {
            for (GuaPledgeOccupyDTO guaPledgeOccupyDTO : GuaPledgeOccupyDTO) {
                if (GuaranteeConstant.GUA_TYPE_DY.equals(guaPledgeOccupyDTO.getPledgeAttr())) {
                    guaPledgeOccupyDTO.setAvailMoney(guaPledgeOccupyDTO.getPledgeMoney());
                }
            }
        } else {
            for (GuaPledgeOccupyDTO guaPledgeOccupyDTO : GuaPledgeOccupyDTO) {
                if (GuaranteeConstant.GUA_TYPE_DY.equals(guaPledgeOccupyDTO.getPledgeAttr())) {
                    guaPledgeOccupyDTO.setAvailMoney(BigDecimal.ZERO);
                }
            }
        }
        return GuaPledgeOccupyDTO;
    }
    @Override
    public List<GuaPledgeOccupyDTO> getPledgeOccupyListFrozen(String pledgeId) {
        List<GuaPledgeOccupyVO> guaPledgeOccupyVOS = pledgeOccupyService.getPledgeOccupyListFrozen(pledgeId);
        return BeanUtils.copyListProperties(guaPledgeOccupyVOS, GuaPledgeOccupyDTO.class);
    }
    @Override
    public List<GuaPledgeOccupyDTO> getPledgeOccupyList(String pledgeId) {
        List<GuaPledgeOccupyVO> guaPledgeOccupyVOS = pledgeOccupyService.getPledgeOccupyList(pledgeId);
        return BeanUtils.copyListProperties(guaPledgeOccupyVOS, GuaPledgeOccupyDTO.class);
    }
}
