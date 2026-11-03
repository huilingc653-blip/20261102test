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
import com.bytter.guarantee.domain.dao.ContractSuspendDao;
import com.bytter.guarantee.domain.entity.ConInfo;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ContractSuspendVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.guarantee.enums.ContractSuSpendEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @Title: 合同终止service
 * @Package com.bytter.biz.contract.service
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-12 下午7:28:38
 * @version V1.0
 */
@WorkFlow(ConWorkFlowConstant.CON_CONSTRACT_SUSPEND)
@DubboService(interfaceClass = IWorkFlowListener.class,group = ConWorkFlowConstant.CON_CONSTRACT_SUSPEND)
@Service("contractSuspendService")
@Transactional
public class ContractSuspendService extends BaseService<ContractSuspendVO, String> implements IWorkFlowListener {
    @Autowired
    private ContractSuspendDao contractSuspendDao;
    @Autowired
    private ConCorpService conCorpService;
    @Autowired
    private ConInfoRecordService conInfoRecordService;
    @Autowired
    private ConLogService conLogService;
    @Autowired
    private ConInfoService conInfoService;
    @Autowired
    private ConWorkFlowService conWorkFlowService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;

    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = conCorpService.getUserCorpIds(ConConstant.CONTRACT_SUSPEND_MANAGE_OPT,
                input.getCurrentUserId());
        DataTablesOutput<ContractSuspendVO> dataTablesOutput = contractSuspendDao.getDataTablesUnion(input, corpAuthList, input.getCurrentUserId());
        List<String> corpIds = dataTablesOutput.getData().stream().map(ContractSuspendVO::getCorpId).collect(Collectors.toList());
        Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(corpIds);
        for (ContractSuspendVO datum : dataTablesOutput.getData()) {
            datum.setCorpCode(corpDTOMap.get(datum.getCorpId()).getCode());
        }
        return dataTablesOutput;
    }
    /**
     *
     * @Title: singleDispose @Description: TODO(单条业务处理) @param @param ids @param @param opt @param @param reason
     *         参数 @return void 返回类型 @throws
     */
    public void singleDispose(ContractSuspendVO contractSuspendVO, String opt, ICurrentUser userVo) {
        ConInfoRecord conInfoRecord = conInfoRecordService.get(contractSuspendVO.getRid());
        ConInfo conInfo = conInfoService.get(conInfoRecord.getMid());
        String approveRecorId = null;
        if (!conInfo.getStatus().equals(ConConstant.CON_INFO_STATUS_80)) {
            conInfoService.updateConInfoStatusById(conInfoRecord.getMid(), ConConstant.CON_INFO_STATUS_80);
            // 设置前一笔流水不可用，新增一笔现在的流水
            conInfoRecordService.updateRecordById(contractSuspendVO.getRid(), null, ConConstant.CON_VALID_SIGN_2,
                    userVo);
            ConInfoRecord tempRevordVo = new ConInfoRecord();
            BeanUtils.copyNotNullProperties(conInfoRecord, tempRevordVo);
            tempRevordVo.setId(null);
            tempRevordVo.setStatus(opt.equals(ContractSuSpendEnum.submit.name()) ? ConConstant.CON_SUSPEND_STATUS_11
                    : ConConstant.CON_SUSPEND_STATUS_0);
            tempRevordVo.setBusType(ConConstant.CON_BUS_TYPE_06);
            tempRevordVo.setValidSign(ConConstant.CON_VALID_SIGN_0);
            tempRevordVo.setLastId(conInfoRecord.getId());
            tempRevordVo.setOpMsg(contractSuspendVO.getOpMsg());
            tempRevordVo.setOpDate(contractSuspendVO.getOpDate());
            tempRevordVo.setOpNo(contractSuspendVO.getOpNo());
            tempRevordVo.setCreateId(userVo.getId());
            tempRevordVo.setAttachmentId(contractSuspendVO.getReAttachmentIde());
            conInfoRecordService.saveEntity(tempRevordVo);
            approveRecorId = tempRevordVo.getId();
            if (opt.equals(ContractSuSpendEnum.submit.name()))
                approveRecorId = tempRevordVo.getId();
        } else {
            conInfoRecord.setStatus(opt.equals(ContractSuSpendEnum.submit.name()) ? ConConstant.CON_SUSPEND_STATUS_11
                    : ConConstant.CON_SUSPEND_STATUS_0);
            conInfoRecord.setBusType(ConConstant.CON_BUS_TYPE_06);
            conInfoRecord.setValidSign(ConConstant.CON_VALID_SIGN_0);
            conInfoRecord.setOpMsg(contractSuspendVO.getOpMsg());
            conInfoRecord.setOpDate(contractSuspendVO.getOpDate());
            conInfoRecord.setOpNo(contractSuspendVO.getOpNo());
            conInfoRecord.setCreateId(userVo.getId());
            conInfoRecord.setAttachmentId(contractSuspendVO.getReAttachmentIde());
            conInfoRecordService.updateEntity(conInfoRecord);
            if (opt.equals(ContractSuSpendEnum.submit.name()))
                approveRecorId = conInfoRecord.getId();
        }
        // 提交状态保存数据
        if (opt.equals(ContractSuSpendEnum.submit.name()) && !StringUtils.isBlank(approveRecorId)) {
            // 启动工作流
             getStartHandler().handler(ConWorkFlowConstant.CON_CONSTRACT_SUSPEND, approveRecorId, conInfoRecord,
                    conInfo.getCorpId(), userVo.getId());
            conLogService.saveConLog(conInfoRecord.getMid(), approveRecorId, ConConstant.CON_BUS_TYPE_06,
                    ConConstant.CONLOG_LOGTYPE_SUBMIT, "合同终止提交", userVo);
        }
    }
    /**
     *
     * @Title: getContractSuspend @Description: TODO(查询终止信息) @param @param id @param @param opt @param @return
     *         参数 @return ContractSuspendVO 返回类型 @throws
     */
    public ContractSuspendVO getContractSuspend(String id) {
        ContractSuspendVO conVo = contractSuspendDao.querySuspendById(id);
        Map<String, DictDTO> dictDTOMap = dictService.listMap("CON_TERMINATE_STATUS");
        conVo.setStatusName(dictDTOMap.getOrDefault(String.valueOf(conVo.getStatus()), DictDTO.EMPTY).getName());
        List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService.getConInfoBusDtl(new ConInfoBusdtlVO(conVo.getMid()));
        List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
        conVo.setBusdtlList(busdtlList);
        return conVo;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = conWorkFlowService
                .getWorkFlowBusiness(ConWorkFlowConstant.CON_CONSTRACT_SUSPEND, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        // 调用公共查询当前审批人审批
        DataTablesOutput dp = contractSuspendDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
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
            // 修改主表状态为已终止
            conInfoService.updateConInfoStatusById(conInfoRecord.getMid(), ConConstant.CON_INFO_STATUS_90);
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
        conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_06, logType,
                appMsg, workFlowResult.getUser());
    }
    /**
     *
     * @Title: delConSuSpend @Description: TODO(合同终止删除) @param @param id @param @param userVo 参数 @return void
     *         返回类型 @throws
     */
    public void delConSuSpend(String id, String lastId, ICurrentUser userVo) {
        ConInfoRecord conInfoRecord = conInfoRecordService.get(id);
        // 设置上一流水状态为有效
        if (StringUtils.isNotEmpty(lastId)) {
            ConInfoRecord conInfoLastRecord = conInfoRecordService.get(lastId);
            conInfoLastRecord.setValidSign(ConConstant.CON_VALID_SIGN_1);
            conInfoRecordService.updateEntity(conInfoLastRecord);
        }
        // 合同状态变为已签订
        conInfoService.updateConInfoStatusById(conInfoRecord.getMid(), ConConstant.CON_INFO_STATUS_50);
        conInfoRecordService.updateRecordById(id, ConConstant.CON_SUSPEND_STATUS_99, ConConstant.CON_VALID_SIGN_2,
                userVo);
        conLogService.saveConLog(conInfoRecord.getMid(), id, ConConstant.CON_BUS_TYPE_06,
                ConConstant.CONLOG_LOGTYPE_DELETE, "合同终止-删除", userVo);
    }
    /**
     * @Title: getQueryDataTablesUnion @Description: TODO( * 获取查询合同信息列表) @param @param input @param @return 参数 @return
     *         DataTablesOutput 返回类型 @throws
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = conCorpService.getUserCorpIds(ConConstant.CONTRACT_OVERVIEW_QUERY_OPT,
                input.getCurrentUserId());
        DataTablesOutput<ContractSuspendVO> dataTablesOutput = contractSuspendDao.getQueryDataTablesUnion(input, corpAuthList, input.getCurrentUserId());
        List<String> corpIds = dataTablesOutput.getData().stream().map(ContractSuspendVO::getCorpId).collect(Collectors.toList());
        Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(corpIds);
        for (ContractSuspendVO contractSuspendVO : dataTablesOutput.getData()) {
            contractSuspendVO.setCorpCode(corpDTOMap.get(contractSuspendVO.getCorpId()).getCode());
        }
        return dataTablesOutput;
    }
    @Override
    public BaseDao<ContractSuspendVO, String> getEntityDao() {
        return null;
    }
}
