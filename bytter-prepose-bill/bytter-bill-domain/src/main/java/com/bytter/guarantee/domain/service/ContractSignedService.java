package com.bytter.guarantee.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.constant.ConWorkFlowConstant;
import com.bytter.guarantee.domain.dao.ConDraftDao;
import com.bytter.guarantee.domain.dao.ContractSignedDao;
import com.bytter.guarantee.domain.entity.ConInfo;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.guarantee.domain.vo.ConSignVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @Title: 合同签署 SignedService
 * @Package com.bytter.biz.contract.service
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-9 下午1:43:41
 */
@WorkFlow(ConWorkFlowConstant.CON_CONSTRACT_SIGN)
@DubboService(interfaceClass = IWorkFlowListener.class,group = ConWorkFlowConstant.CON_CONSTRACT_SIGN)
@Service("contractSignedService")
@Transactional
public class ContractSignedService extends BaseService<ConSignVO, String> implements IWorkFlowListener {
    @Autowired
    private ConTypeService conTypeService;
    @Autowired
    private ConDraftDao conDraftDao;
    @Autowired
    private ConCorpService conCorpService;
    @Autowired
    private ContractSignedDao contractSignedDao;
    @Autowired
    private ConWorkFlowService conWorkFlowService;
    @Autowired
    private ConLogService conLogService;
    @Autowired
    private ConInfoService conInfoService;
    @Autowired
    private ConInfoRecordService conInfoRecordService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, String userId) {
        List<String> corpAuthList = conCorpService.getUserCorpIds(ConConstant.CONTRACT_SIGNED_MANAGE_OPT,
                input.getCurrentUserId());
        DataTablesOutput<ConSignVO> dataTablesOutput = contractSignedDao.getDataTablesUnion(input, corpAuthList, userId);
        List<String> corpIds = dataTablesOutput.getData().stream().map(ConSignVO::getCorpId).collect(Collectors.toList());
        Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(corpIds);
        for (ConSignVO datum : dataTablesOutput.getData()) {
            datum.setCorpCode(corpDTOMap.get(datum.getCorpId()).getCode());
        }
        return dataTablesOutput;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = conWorkFlowService
                .getWorkFlowBusiness(ConWorkFlowConstant.CON_CONSTRACT_SIGN, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        // 调用公共查询当前审批人审批
        DataTablesOutput dp = conDraftDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    /**
     * @Title: singleDispose @Description: TODO(单条业务处理) @param @param ids @param @param opt @param @param reason
     * 参数 @return void 返回类型 @throws
     */
    public void singleDispose(String rid, String opt, ICurrentUser userVo, String reason) {
        ConInfoRecord conInfoRecord = conInfoRecordService.get(rid);
        ConInfo conInfo = conInfoService.get(conInfoRecord.getMid());
        // 拒绝签署
        if (opt.equals(ConConstant.CONTRACT_SIGNED_REFUSED_POWER)) {
            conInfoService.updateConInfoStatusById(conInfoRecord.getMid(), ConConstant.CON_INFO_STATUS_10);
            conInfoRecord.setStatus(ConConstant.CON_DRAFT_STATUS_2);
            conInfoRecord.setBusType(ConConstant.CON_BUS_TYPE_01);
            conInfoRecord.setValidSign(ConConstant.CON_VALID_SIGN_0);
            conInfoRecord.setBackMsg(reason);
            conInfoRecordService.updateEntity(conInfoRecord);
            conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_02,
                    ConConstant.CONLOG_LOGTYPE_SIGNREPULSE, "合同签署拒绝", userVo);
        } else {
            // 同意签署
            conInfoRecordService.updateRecordById(rid, null, ConConstant.CON_VALID_SIGN_2, userVo);
            // 新增一笔流水
            ConInfoRecord newsRevordVo = new ConInfoRecord();
            BeanUtils.copyNotNullProperties(conInfoRecord, newsRevordVo);
            newsRevordVo.setId(null);
            newsRevordVo.setStatus(ConConstant.CON_DRAFT_STATUS_11);
            newsRevordVo.setBusType(ConConstant.CON_BUS_TYPE_02);
            newsRevordVo.setValidSign(ConConstant.CON_VALID_SIGN_0);
            newsRevordVo.setLastId(rid);
            conInfoRecordService.saveEntity(newsRevordVo);
            conInfoService.updateConInfoStatusById(conInfoRecord.getMid(), ConConstant.CON_INFO_STATUS_30);
            // 启动工作流
             getStartHandler().handler(ConWorkFlowConstant.CON_CONSTRACT_SIGN, newsRevordVo.getId(), newsRevordVo,
                    conInfo.getCorpId(), userVo.getId());
            conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_02,
                    ConConstant.CONLOG_LOGTYPE_SUBMIT, "合同签署提交", userVo);
        }
    }
    /**
     * @Title: conSignedOptWith @Description: TODO(批量处理业务流程) @param @return 参数 @return boolean 返回类型 @throws
     */
    public void batchConSignedOptWith(String ids, String opt, String reason, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] ridArray = ids.split(",");
            for (String rid : ridArray) {
                if (!StringUtils.isBlank(rid)) {
                    this.singleDispose(rid, opt, userVo, reason);
                }
            }
        }
    }
    @Override
    public BaseDao<ConSignVO, String> getEntityDao() {
        return null;
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? ConConstant.CON_LOG_APP_SUCC : ConConstant.CON_LOG_APP_BACK;
        ConInfoRecord conInfoRecord = conInfoRecordService.get(workFlowResult.getBusinessKey(),true);
        int num = 0;
        if (workFlowResult.isPass()) {
            /** 审批通过 */
            // 修改主表状态为已签订
            conInfoService.updateConInfoById(conInfoRecord.getMid(), ConConstant.CON_INFO_STATUS_50);
            // 修改流水表为审批通过,业务完成
            num = conInfoRecordService.updateRecordById(conInfoRecord.getId(), ConConstant.CON_DRAFT_STATUS_95,
                    ConConstant.CON_VALID_SIGN_1, workFlowResult.getUser());
        } else {
            /** 打回 */
            num = conInfoRecordService.updateRecordById(conInfoRecord.getId(), ConConstant.CON_DRAFT_STATUS_1,
                    ConConstant.CON_VALID_SIGN_0, workFlowResult.getUser());
        }
        if (num != 1) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        String logType = workFlowResult.isPass() ? ConConstant.CONLOG_LOGTYPE_APP : ConConstant.CONLOG_LOGTYPE_REPULSE;
        conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_02, logType,
                appMsg, workFlowResult.getUser());
    }
}
