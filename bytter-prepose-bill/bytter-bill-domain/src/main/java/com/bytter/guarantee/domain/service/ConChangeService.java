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
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.constant.ConWorkFlowConstant;
import com.bytter.guarantee.domain.dao.ConChangeDao;
import com.bytter.guarantee.domain.entity.ConInfo;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.guarantee.domain.vo.ConChangeVO;
import com.bytter.guarantee.domain.vo.ConInfoRecordVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Maps;
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
 * @Title: 合同变更业务Service
 * @Description: 合同变更业务处理类
 * @date 2019-11-12
 */
@WorkFlow(ConWorkFlowConstant.CON_CONSTRACT_CHANGE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = ConWorkFlowConstant.CON_CONSTRACT_CHANGE)
@Service("conChangeService")
@Transactional
public class ConChangeService extends BaseService<ConChangeVO, String> implements IWorkFlowListener {
    @Autowired
    private ConChangeDao conChangeDao;
    @Autowired
    private ConCorpService conCorpService;
    @Autowired
    private ConLogService conLogService;
    @Autowired
    private ConInfoService conInfoService;
    @Autowired
    private ConInfoRecordService conInfoRecordService;
    @Autowired
    private ConWorkFlowService conWorkFlowService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<ConChangeVO, String> getEntityDao() {
        return null;
    }
    /**
     * 合同变更列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, String userId) {
        List<String> corpAuthList = conCorpService.getUserCorpIds(ConConstant.CON_CONCHANGE_RESOURCE_OP,
                input.getCurrentUserId());
        return conChangeDao.getDataTablesUnion(input, corpAuthList, userId);
    }
    /**
     * 逻辑删除变更信息
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
                    // 回滚主表状态为已签订
                    conInfoService.updateConInfoStatusById(infoRecord.getMid(), ConConstant.CON_INFO_STATUS_50);
                    // 修改上一笔流水为1可用
                    conInfoRecordService.updateRecordById(infoRecord.getLastId(), null, ConConstant.CON_VALID_SIGN_1,
                            userVo);
                    // 修改合同变更流水为删除状态
                    int num = conInfoRecordService.updateRecordById(infoRecord.getId(), ConConstant.CON_OTHER_STATUS_99,
                            ConConstant.CON_VALID_SIGN_0, userVo);
                    if (num == 1) {
                        conLogService.saveConLog(infoRecord.getMid(), infoRecord.getId(), ConConstant.CON_BUS_TYPE_04,
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
     * 功能介绍 : 暂存合同变更<br>
     * <b>日期：</b>2019年11月12日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void saveChangeApply(ConChangeVO changeVO, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(changeVO, ConConstant.CON_OPERATE_SAVE, userVo);
    }
    /**
     * 功能介绍 : 修改合同变更<br>
     * <b>日期：</b>2019年11月12日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void updateChangeApply(ConChangeVO changeVO, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(changeVO, ConConstant.CON_OPERATE_EDIT, userVo);
    }
    /**
     * 功能介绍 : 修改合同变更<br>
     * <b>日期：</b>2019年11月12日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void submitChangeApply(ConChangeVO changeVO, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(changeVO, ConConstant.CON_OPERATE_SUBMIT, userVo);
    }
    /**
     * 功能介绍 : 保存或提交合同变更<br>
     * <b>日期：</b>2019年11月12日<br>
     *
     * @param optrate 保存或提交标志
     * @version 1.0
     * @author zhenghj
     */
    public void saveOrUpdateOrSubmit(ConChangeVO changeVO, String optrate, ICurrentUser userVo) {
        if (changeVO != null) {
            ConInfo conInfo = null;
            ConInfoRecord conInfoRecord = null;
            ConInfoRecord conInfoRecordLast = null;
            if (StringUtils.isEmpty(changeVO.getRid()) && StringUtils.isNotEmpty(changeVO.getMid())) {
                // 合同状态为变更中
                conInfoService.updateConInfoStatusById(changeVO.getMid(), ConConstant.CON_INFO_STATUS_70);
                // 根据合同主表id获取可操作的流水(签署完成,登记完成)
                conInfoRecordLast = getConInfoRecordByMid(changeVO.getMid());
                // 修改上一笔流水为2已作废
                conInfoRecordService.updateRecordById(conInfoRecordLast.getId(), null, ConConstant.CON_VALID_SIGN_2,
                        userVo);
                // 保存合同变更流水
                conInfoRecord = getConInfoRecord(changeVO, ConConstant.CON_OPERATE_SAVE, userVo);
                conInfoRecord.setLastId(conInfoRecordLast.getId());
                // 默认为暂存,当为提交状态时,设置流水状态为待审批
                if (ConConstant.CON_OPERATE_SUBMIT.equals(optrate)) {
                    conInfoRecord.setStatus(ConConstant.CON_DRAFT_STATUS_11);// 待审批
                }
                conInfoRecordService.saveEntity(conInfoRecord);
            } else {
                // 编辑更新或者编辑提交处理
                // 更新合同变更流水
                conInfoRecord = conInfoRecordService.get(changeVO.getRid());
                conInfoRecord.setAttachmentId(changeVO.getAttachmentId());
                conInfoRecord.setOpMsg(changeVO.getOpMsg());
                conInfoRecord.setOpDate(changeVO.getOpDate());
                conInfoRecord.setRmk(changeVO.getRmk());
                conInfoRecord.setUpdateId(userVo.getId());
                conInfoRecord.setUpdateTime(new Date());
                // 默认为暂存,当为提交状态时,设置流水状态为待审批
                if (ConConstant.CON_OPERATE_SUBMIT.equals(optrate)) {
                    conInfoRecord.setStatus(ConConstant.CON_DRAFT_STATUS_11);// 待审批
                }
                conInfoRecordService.updateEntity(conInfoRecord);
            }
            // 提交则启动工作流
            if (ConConstant.CON_OPERATE_SUBMIT.equals(optrate)) {
                conInfo = conInfoService.get(changeVO.getMid());
                // 启动工作流
                 getStartHandler().handler(ConWorkFlowConstant.CON_CONSTRACT_CHANGE, conInfoRecord.getId(), changeVO,
                        conInfo.getCorpId(), userVo.getId());
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_04,
                        ConConstant.CONLOG_LOGTYPE_SUBMIT, "合同变更提交", userVo);
            } else if (ConConstant.CON_OPERATE_EDIT.equals(optrate)) {
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_04,
                        ConConstant.CONLOG_LOGTYPE_UPDATE, "合同变更修改", userVo);
            } else {
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_04,
                        ConConstant.CONLOG_LOGTYPE_ADD, "合同变更暂存", userVo);
            }
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
            // 修改主表状态为已签订和对合同变更次数进行叠加操作
            ConInfoVO conInfoVO = new ConInfoVO();
            conInfoVO.setId(conInfoRecord.getMid());
            conInfoVO.setIsChangeNum("1");
            conInfoVO.setStatus(ConConstant.CON_INFO_STATUS_50);
            conInfoService.updateConInfoByConInfoVO(conInfoVO);
            // 回滚上一笔流水为可用
            conInfoRecordService.updateRecordById(conInfoRecord.getLastId(), null, ConConstant.CON_VALID_SIGN_1,
                    workFlowResult.getUser());
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
        conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_04, logType,
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
                .getWorkFlowBusiness(ConWorkFlowConstant.CON_CONSTRACT_CHANGE, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<ConChangeVO> dp = conChangeDao.dataTableApprove(dataTablesInput, businessList);
        setDictField(dp.getData());
        return workFlowBusiness.merge(dp);
    }

    private void setDictField(List<ConChangeVO> conChangeVOS) {
        if (CollectionUtil.isEmpty(conChangeVOS)) {
            return;
        }
        Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("CON_DRAFT_STATUS", "CON_SIGN_TYPE");
        for (ConChangeVO datum : conChangeVOS) {
            datum.setStatusName(dictMap.getOrDefault("CON_DRAFT_STATUS", Maps.newHashMap()).getOrDefault(String.valueOf(datum.getStatus()), DictDTO.EMPTY).getName());
            datum.setSignTypeName(dictMap.getOrDefault("CON_SIGN_TYPE", Maps.newHashMap()).getOrDefault(datum.getSignType(), DictDTO.EMPTY).getName());
        }
    }
    /**
     * 根据流水Id查询合同变更信息
     *
     * @param rid 合同变更流水id
     * @return
     */
    public ConChangeVO getConChangeVOByRid(String rid) {
        if (StringUtils.isNotEmpty(rid)) {
            List<ConChangeVO> list = conChangeDao.getConChangeVOByRid(rid);
            setDictField(list);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }
    /**
     * 根据主表Id查询合同流水信息
     *
     * @param mid 合同主表id
     * @return
     */
    public ConInfoRecord getConInfoRecordByMid(String mid) {
        if (StringUtils.isNotEmpty(mid)) {
            String[] busTypes = {ConConstant.CON_BUS_TYPE_02, ConConstant.CON_BUS_TYPE_03};
            List<ConInfoRecord> conRecordList = conInfoRecordService.getConInfoRecord(
                    new ConInfoRecordVO(mid, busTypes, ConConstant.CON_DRAFT_STATUS_95, ConConstant.CON_VALID_SIGN_1));
            if (conRecordList != null && !conRecordList.isEmpty()) {
                return conRecordList.get(0);
            }
        }
        return null;
    }
    /**
     * 根据提交参数生成ConInfoRecord 合同变更流水信息
     *
     * @param changeVO 页面传递登记信息
     * @param optrate  保存或提交标志
     * @param userVo   操作用户
     * @return ConInfoRecord
     */
    protected ConInfoRecord getConInfoRecord(ConChangeVO changeVO, String optrate, ICurrentUser userVo) {
        ConInfoRecord entity = new ConInfoRecord();
        entity.setMid(changeVO.getMid());// 主信息ID
        entity.setAttachmentId(changeVO.getAttachmentId());
        entity.setBusType(ConConstant.CON_BUS_TYPE_04);// 变更中
        entity.setValidSign(ConConstant.CON_VALID_SIGN_0);// 业务中
        entity.setStatus(ConConstant.CON_DRAFT_STATUS_0);// 暂存
        entity.setOpMsg(changeVO.getOpMsg());// 变更原因
        entity.setOpDate(changeVO.getOpDate());// 变更时间
        entity.setRmk(changeVO.getRmk());// 备注
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
