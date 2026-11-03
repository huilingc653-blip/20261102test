package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.GuaWorkFlowConstant;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaContractInfoDao;
import com.bytter.guarantee.domain.entity.GuaContractInfo;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaExtContractEndService
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/5/11 14:59
 */
@Service("guaExtContractEndService")
@Transactional
@WorkFlow(value = { GuaWorkFlowConstant.GUA_EXTCONTRACT_END })
@DubboService(interfaceClass = IWorkFlowListener.class,group = GuaWorkFlowConstant.GUA_EXTCONTRACT_END )
public class GuaExtContractEndService extends BaseService implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private GuaExtContractInfoService guaExtContractInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private GuaContractInfoDao guaContractInfoDao;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService guaPledgeOccupyServiceImpl;
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
    /**
     * 根据id终止合同
     *
     * @param id
     */
    public void terminate(String id, ICurrentUser user) {
        GuaContractInfo guaContractInfo = guaExtContractInfoService.get(id);
        guaContractInfo.setStatus(GuaranteeConstant.STATUS_ENDING);
        guaExtContractInfoService.saveOrUpdate(guaContractInfo);
         getStartHandler().handler(GuaWorkFlowConstant.GUA_EXTCONTRACT_END, guaContractInfo.getId(), guaContractInfo,
                guaContractInfo.getCorpId(), user.getId());
    }
    /**
     * 取消终止合同
     */
    public void cancelEnd(String id) {
        GuaContractInfo guaContractInfo = guaExtContractInfoService.get(id);
        guaContractInfo.setStatus(GuaranteeConstant.STATUS_CHECK);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        GuaContractInfo guaContractInfo = guaExtContractInfoService.get(businessKey,true);
        if (workFlowResult.isPass()) {
            guaContractInfo.setStatus(GuaranteeConstant.STATUS_END);// 已终止
            guaPledgeOccupyServiceImpl.releasePledgeOccupy(guaContractInfo.getId(), guaContractInfo.getGuaNo());
        } else {
            guaContractInfo.setStatus(GuaranteeConstant.STATUS_END_BACK);// 终止打回
        }
        guaExtContractInfoService.saveOrUpdate(guaContractInfo);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = guaContractInfoDao.queryBusBillByCorpId(corpId, GuaranteeConstant.CON_TYPE_1, "104");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DISABLE_EXTCONTRACTEND_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
