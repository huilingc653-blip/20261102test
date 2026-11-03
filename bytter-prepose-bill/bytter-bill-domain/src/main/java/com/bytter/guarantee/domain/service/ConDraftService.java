package com.bytter.guarantee.domain.service;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
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
import com.bytter.guarantee.domain.entity.ConInfo;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.guarantee.domain.vo.ConDraftVO;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同起草业务Service
 * @Description: 合同起草业务处理类
 * @date 2019-11-04
 */
@WorkFlow(ConWorkFlowConstant.CON_CONSTRACT_DRAFT)
@DubboService(interfaceClass = IWorkFlowListener.class,group = ConWorkFlowConstant.CON_CONSTRACT_DRAFT)
@Service("conDraftService")
@Transactional
public class ConDraftService extends BaseService<ConDraftVO, String> implements IWorkFlowListener {
    @Autowired
    private ConDraftDao conDraftDao;
    @Autowired
    private ConCorpService conCorpService;
    @Autowired
    private ConLogService conLogService;
    @Autowired
    private ConInfoService conInfoService;
    @Autowired
    private ConInfoRecordService conInfoRecordService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConWorkFlowService conWorkFlowService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<ConDraftVO, String> getEntityDao() {
        return null;
    }
    /**
     * 合同起草列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, String userId) {
        List<String> corpAuthList = conCorpService.getUserCorpIds(ConConstant.CON_CONDRAFT_RESOURCE_OP,
                input.getCurrentUserId());
        return conDraftDao.getDataTablesUnion(input, corpAuthList, userId);
    }
    /**
     * 逻辑删除起草信息
     *
     * @param rid    流水表id
     * @param userVo 操作用户
     */
    public boolean batchDelete(String rid, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(rid)) {
            String[] ridArray = rid.split(",");
            ConInfoRecord infoRecord = null;
            for (int i = 0; i < ridArray.length; i++) {
                infoRecord = conInfoRecordService.get(ridArray[i]);
                if (infoRecord != null) {
                    // 修改主表状态为已删除
                    conInfoService.updateConInfoStatusById(infoRecord.getMid(), ConConstant.CON_OTHER_STATUS_99);
                    int num = conInfoRecordService.updateRecordById(infoRecord.getId(), ConConstant.CON_OTHER_STATUS_99,
                            ConConstant.CON_VALID_SIGN_0, userVo);
                    if (num == 1) {
                        conLogService.saveConLog(infoRecord.getMid(), infoRecord.getId(), ConConstant.CON_BUS_TYPE_01,
                                ConConstant.CONLOG_LOGTYPE_DELETE, ConConstant.LANG_LOG_DELETE_SUCC, userVo);
                        return true;
                    } else {
                        throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                }
            }
        }
        return false;
    }
    /**
     * 功能介绍 : 暂存合同起草<br>
     * <b>日期：</b>2019年11月07日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void saveDraftApply(ConDraftVO draftVo, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(draftVo, ConConstant.CON_OPERATE_SAVE, userVo);
    }
    /**
     * 功能介绍 : 修改合同起草<br>
     * <b>日期：</b>2019年11月07日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void updateDraftApply(ConDraftVO draftVo, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(draftVo, ConConstant.CON_OPERATE_EDIT, userVo);
    }
    /**
     * 功能介绍 : 修改合同起草<br>
     * <b>日期：</b>2019年11月07日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void submitDraftApply(ConDraftVO draftVo, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(draftVo, ConConstant.CON_OPERATE_SUBMIT, userVo);
    }
    /**
     * 功能介绍 : 保存或提交合同起草<br>
     * <b>日期：</b>2019年11月07日<br>
     *
     * @param optrate 保存或提交标志
     * @version 1.0
     * @author zhenghj
     */
    public void saveOrUpdateOrSubmit(ConDraftVO draftVo, String optrate, ICurrentUser userVo) {
        if (draftVo != null) {
            ConInfo conInfo = null;
            ConInfoRecord conInfoRecord = null;
            /** 新增处理 */
            if (StringUtils.isEmpty(draftVo.getMid())) {
                // 保存合同主信息
                conInfo = getConInfo(draftVo);
                conInfoService.saveEntity(conInfo);
                draftVo.setMid(conInfo.getId());
                // 保存合同起草流水
                conInfoRecord = getConInfoRecord(draftVo, ConConstant.CON_OPERATE_SAVE, userVo);
                // 默认为暂存,当为提交状态时,设置流水状态为待审批
                if (ConConstant.CON_OPERATE_SUBMIT.equals(optrate)) {
                    conInfoRecord.setStatus(ConConstant.CON_DRAFT_STATUS_11);// 待审批
                }
                conInfoRecordService.saveEntity(conInfoRecord);
                // 保存合同起草字段明细
                List<ConInfoBusdtlVO> busdtls = draftVo.getBusdtls();
                saveBusDtl(conInfo, busdtls);

            } else {
                // 编辑更新或者编辑提交处理
                // 获取合同主信息
                conInfo = conInfoService.get(draftVo.getMid());
                ConInfo conInfoNew = getConInfo(draftVo);
                BeanUtils.copyNotNullProperties(conInfoNew, conInfo);
                conInfoService.updateEntity(conInfo);
                // 保存合同起草流水
                conInfoRecord = conInfoRecordService.get(draftVo.getRid());
                // conInfoRecord.setAttachmentId(draftVo.getAttachmentId());
                conInfoRecord.setBackMsg(null);
                conInfoRecord.setUpdateId(userVo.getId());
                conInfoRecord.setUpdateTime(new Date());
                // 默认为暂存,当为提交状态时,设置流水状态为待审批
                if (ConConstant.CON_OPERATE_SUBMIT.equals(optrate)) {
                    conInfoRecord.setStatus(ConConstant.CON_DRAFT_STATUS_11);// 待审批
                }
                conInfoRecordService.updateEntity(conInfoRecord);
                // 删除合同起草字段明细
                conInfoBusdtlService.deleteConInfoBusDtlByMid(conInfo.getId());
                // 保存合同起草字段明细
                List<ConInfoBusdtlVO> busdtls = draftVo.getBusdtls();
                //明细可能为空
                saveBusDtl(conInfo, busdtls);
            }
            // 提交则启动工作流
            if (ConConstant.CON_OPERATE_SUBMIT.equals(optrate)) {
                // todo:启动工作流
                 getStartHandler().handler(ConWorkFlowConstant.CON_CONSTRACT_DRAFT, conInfoRecord.getId(), draftVo,
                        conInfo.getCorpId(), userVo.getId());
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_01,
                        ConConstant.CONLOG_LOGTYPE_SUBMIT, "合同起草提交", userVo);
            } else if (ConConstant.CON_OPERATE_EDIT.equals(optrate)) {
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_01,
                        ConConstant.CONLOG_LOGTYPE_UPDATE, "合同起草修改", userVo);
            } else {
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_01,
                        ConConstant.CONLOG_LOGTYPE_ADD, "合同起草暂存", userVo);
            }
        }
    }

    /**
     * 保存合同明细
     * @param conInfo
     * @param busdtls
     */
    private void saveBusDtl(ConInfo conInfo, List<ConInfoBusdtlVO> busdtls) {
        if (CollectionUtil.isEmpty(busdtls)) {
            throw new BusinessException(ConMessageEnum.CONSTACT_BUS_KEY_MESSAGE_ERROR);
        }
        for (ConInfoBusdtlVO busdtlVO : busdtls) {
            ConInfoBusDtl busdtl = new ConInfoBusDtl();
            BeanUtils.copyNotNullProperties(busdtlVO, busdtl);
            busdtl.setMid(conInfo.getId());
            conInfoBusdtlService.saveEntity(busdtl);
        }
    }

    /**
     * 审批回调方法
     *
     * @param workFlowResult processDefinitionKey 工作流类型key businessKey 业务类型key result 审批结果:true审批通过，false审批打回
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? ConConstant.CON_LOG_APP_SUCC : ConConstant.CON_LOG_APP_BACK;
        ConInfoRecord conInfoRecord = conInfoRecordService.get(workFlowResult.getBusinessKey(),true);
        int num = 0;
        if (workFlowResult.isPass()) {
            /** 审批通过 */
            // 修改主表状态为已起草
            conInfoService.updateConInfoStatusById(conInfoRecord.getMid(), ConConstant.CON_INFO_STATUS_20);
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
        conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_01, logType,
                appMsg, workFlowResult.getUser());
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
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
                .getWorkFlowBusiness(ConWorkFlowConstant.CON_CONSTRACT_DRAFT, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = conDraftDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据流水Id查询合同起草信息
     *
     * @param rid 合同起草流水id
     * @return
     */
    public ConDraftVO getConDraftVOByRid(String rid) {
        if (StringUtils.isNotEmpty(rid)) {
            List<ConDraftVO> list = conDraftDao.getConDraftVOByRid(rid);
            if (list != null && !list.isEmpty()) {
                Map<String, DictDTO> dictDTOMap = dictService.listMap("CON_SIGN_TYPE");
                ConDraftVO conDraftVO = list.get(0);
                conDraftVO.setSignTypeName(dictDTOMap.getOrDefault(conDraftVO.getSignType(), DictDTO.EMPTY).getName());
                return conDraftVO;
            }
        }
        return null;
    }
    /**
     * 根据提交参数生成ConInfo 合同主信息
     *
     * @param conDraftVO 页面传递起草信息
     * @return ConInfo
     */
    protected ConInfo getConInfo(ConDraftVO conDraftVO) {
        ConInfo entity = new ConInfo();
        entity.setConNo(conDraftVO.getConNo());// 合同编号
        entity.setConName(conDraftVO.getConName());// 合同名称
        entity.setCorpId(conDraftVO.getCorpId());// 合同发起单位
        entity.setSignType(StringUtils.isNotEmpty(conDraftVO.getSignType()) ? conDraftVO.getSignType()
                : ConConstant.CON_SIGN_TYPE_0);// 签署方式
        entity.setTypeId(conDraftVO.getTypeId());// 合同类型Id
        entity.setBeginDate(conDraftVO.getBeginDate());// 合同生效日期
        entity.setEndDate(conDraftVO.getEndDate());// 合同失效日期
        entity.setAttachmentId(conDraftVO.getAttachmentId());// 附件id
        entity.setStatus(ConConstant.CON_INFO_STATUS_10);// 起草中
        entity.setDraftDate(new Date());
        entity.setCorpName(conDraftVO.getCorpName());
        entity.setCorpCode(conDraftVO.getCorpCode());
        return entity;
    }
    /**
     * 根据提交参数生成ConInfoRecord 合同起草流水信息
     *
     * @param conDraftVO 页面传递起草信息
     * @param optrate    保存或提交标志
     * @param userVo     操作用户
     * @return ConInfoRecord
     */
    protected ConInfoRecord getConInfoRecord(ConDraftVO conDraftVO, String optrate, ICurrentUser userVo) {
        ConInfoRecord entity = new ConInfoRecord();
        entity.setMid(conDraftVO.getMid());// 主信息ID
        // entity.setAttachmentId(conDraftVO.getAttachmentId());
        entity.setBusType(ConConstant.CON_BUS_TYPE_01);// 起草
        entity.setValidSign(ConConstant.CON_VALID_SIGN_0);// 业务中
        entity.setStatus(ConConstant.CON_DRAFT_STATUS_0);// 暂存
        if (ConConstant.CON_OPERATE_SAVE.equals(optrate)) {
            if (userVo != null) {
                entity.setCreateId(userVo.getId());// 创建人id
            }
            entity.setCreateTime(new Date());// 创建日期
        } else {
            if (userVo != null) {
                entity.setUpdateId(userVo.getId());// 修改人id
            }
            entity.setUpdateTime(new Date());// 修改日期
        }
        return entity;
    }
}
