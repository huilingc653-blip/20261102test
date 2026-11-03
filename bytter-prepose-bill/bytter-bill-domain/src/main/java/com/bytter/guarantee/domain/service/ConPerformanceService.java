package com.bytter.guarantee.domain.service;

import cn.hutool.core.collection.CollectionUtil;
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
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.constant.ConWorkFlowConstant;
import com.bytter.guarantee.domain.dao.ConPerformanceDao;
import com.bytter.guarantee.domain.entity.ConInfo;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.guarantee.domain.vo.ConInfoRecordVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.domain.vo.ConPerformanceVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
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
import java.util.stream.Collectors;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同履约业务Service
 * @Description: 合同履约业务处理类
 * @date 2019-11-14
 */
@WorkFlow(ConWorkFlowConstant.CON_CONSTRACT_PERFORMANCE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = ConWorkFlowConstant.CON_CONSTRACT_PERFORMANCE)
@Service("conPerformanceService")
@Transactional
public class ConPerformanceService extends BaseService<ConPerformanceVO, String> implements IWorkFlowListener {
    @Autowired
    private ConPerformanceDao conPerformanceDao;
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
    @DubboReference(check = false)
    private ICorpService corpService;
    @Override
    public BaseDao<ConPerformanceVO, String> getEntityDao() {
        return null;
    }
    /**
     * 合同履约列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, String userId) {
        List<String> corpAuthList = conCorpService.getUserCorpIds(ConConstant.CON_CONPERFORMANCE_RESOURCE_OP,
                input.getCurrentUserId());
        DataTablesOutput<ConPerformanceVO> dataTablesOutput = conPerformanceDao.getDataTablesUnion(input, corpAuthList, userId);
        List<String> corpIds = dataTablesOutput.getData().stream().map(ConPerformanceVO::getCorpId).collect(Collectors.toList());
        Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(corpIds);
        for (ConPerformanceVO datum : dataTablesOutput.getData()) {
            datum.setCorpCode(corpDTOMap.get(datum.getCorpId()).getCode());
        }
        return dataTablesOutput;
    }
    /**
     * 逻辑删除履约信息
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
                    // 修改合同履约流水为删除状态
                    int num = conInfoRecordService.updateRecordById(infoRecord.getId(), ConConstant.CON_OTHER_STATUS_99,
                            ConConstant.CON_VALID_SIGN_0, userVo);
                    if (num == 1) {
                        conLogService.saveConLog(infoRecord.getMid(), infoRecord.getId(), ConConstant.CON_BUS_TYPE_05,
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
     * 功能介绍 : 暂存合同履约<br>
     * <b>日期：</b>2019年11月14日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void savePerformanceApply(ConPerformanceVO performanceVO, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(performanceVO, ConConstant.CON_OPERATE_SAVE, userVo);
    }
    /**
     * 功能介绍 : 修改合同履约<br>
     * <b>日期：</b>2019年11月14日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void updatePerformanceApply(ConPerformanceVO performanceVO, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(performanceVO, ConConstant.CON_OPERATE_EDIT, userVo);
    }
    /**
     * 功能介绍 : 修改合同履约<br>
     * <b>日期：</b>2019年11月12日<br>
     *
     * @version 1.0
     * @author zhenghj
     */
    public void submitPerformanceApply(ConPerformanceVO performanceVO, ICurrentUser userVo) {
        saveOrUpdateOrSubmit(performanceVO, ConConstant.CON_OPERATE_SUBMIT, userVo);
    }
    /**
     * 功能介绍 : 保存或提交合同履约<br>
     * <b>日期：</b>2019年11月14日<br>
     *
     * @param optrate 保存或提交标志
     * @version 1.0
     * @author zhenghj
     */
    public void saveOrUpdateOrSubmit(ConPerformanceVO performanceVO, String optrate, ICurrentUser userVo) {
        if (performanceVO != null) {
            ConInfo conInfo = null;
            ConInfoRecord conInfoRecord = null;
            ConInfoRecord conInfoRecordLast = null;
            if (StringUtils.isEmpty(performanceVO.getRid()) && StringUtils.isNotEmpty(performanceVO.getMid())) {
                // 合同状态为履约中
                conInfoService.updateConInfoStatusById(performanceVO.getMid(), ConConstant.CON_INFO_STATUS_60);
                // 根据合同主表id获取可操作的流水(签署完成,登记完成)
                conInfoRecordLast = getConInfoRecordByMid(performanceVO.getMid());
                // 修改上一笔流水为2已作废
                conInfoRecordService.updateRecordById(conInfoRecordLast.getId(), null, ConConstant.CON_VALID_SIGN_2,
                        userVo);
                // 保存合同履约流水
                conInfoRecord = getConInfoRecord(performanceVO, ConConstant.CON_OPERATE_SAVE, userVo);
                conInfoRecord.setLastId(conInfoRecordLast.getId());
                // 默认为暂存,当为提交状态时,设置流水状态为待审批
                if (ConConstant.CON_OPERATE_SUBMIT.equals(optrate)) {
                    conInfoRecord.setStatus(ConConstant.CON_DRAFT_STATUS_11);// 待审批
                }
                conInfoRecordService.saveEntity(conInfoRecord);
            } else {
                // 编辑更新或者编辑提交处理
                // 更新合同履约流水
                conInfoRecord = conInfoRecordService.get(performanceVO.getRid());
                conInfoRecord.setAttachmentId(performanceVO.getAttachmentId());
                conInfoRecord.setOpMsg(performanceVO.getOpMsg());
                conInfoRecord.setOpDate(performanceVO.getOpDate());
                conInfoRecord.setOpNo(performanceVO.getOpNo());// 履约申请编号
                conInfoRecord.setOpMoney(performanceVO.getOpMoney());// 履约金额
                // conInfoRecord.setRmk(performanceVO.getRmk());
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
                conInfo = conInfoService.get(performanceVO.getMid());
                // 启动工作流
                 getStartHandler().handler(ConWorkFlowConstant.CON_CONSTRACT_PERFORMANCE, conInfoRecord.getId(),
                        performanceVO, conInfo.getCorpId(), userVo.getId());
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_05,
                        ConConstant.CONLOG_LOGTYPE_SUBMIT, "合同履约提交", userVo);
            } else if (ConConstant.CON_OPERATE_EDIT.equals(optrate)) {
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_05,
                        ConConstant.CONLOG_LOGTYPE_UPDATE, "合同履约修改", userVo);
            } else {
                conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_05,
                        ConConstant.CONLOG_LOGTYPE_ADD, "合同履约暂存", userVo);
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
            // 修改主表状态为已签订和增加履约次数
            ConInfoVO conInfoVO = new ConInfoVO();
            conInfoVO.setId(conInfoRecord.getMid());
            conInfoVO.setIsPerformanceNum("1");
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
        conLogService.saveConLog(conInfoRecord.getMid(), conInfoRecord.getId(), ConConstant.CON_BUS_TYPE_05, logType,
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
                .getWorkFlowBusiness(ConWorkFlowConstant.CON_CONSTRACT_PERFORMANCE, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<ConPerformanceVO> dp = conPerformanceDao.dataTableApprove(dataTablesInput, businessList);
        setDictField(dp.getData());
        return workFlowBusiness.merge(dp);
    }
    private void setDictField(List<ConPerformanceVO> conPerformanceVOS) {
        if (CollectionUtil.isEmpty(conPerformanceVOS)) {
            return;
        }
        Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("CON_DRAFT_STATUS", "CON_SIGN_TYPE");
        for (ConPerformanceVO datum : conPerformanceVOS) {
            datum.setStatusName(dictMap.getOrDefault("CON_DRAFT_STATUS", Maps.newHashMap()).getOrDefault(String.valueOf(datum.getStatus()), DictDTO.EMPTY).getName());
            datum.setSignTypeName(dictMap.getOrDefault("CON_SIGN_TYPE", Maps.newHashMap()).getOrDefault(datum.getSignType(), DictDTO.EMPTY).getName());
        }
    }

    /**
     * 根据流水Id查询合同履约信息
     *
     * @param rid 合同履约流水id
     * @return
     */
    public ConPerformanceVO getConPerformanceVOByRid(String rid) {
        if (StringUtils.isNotEmpty(rid)) {
            List<ConPerformanceVO> list = conPerformanceDao.getConPerformanceVOByRid(rid);
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
     * 根据提交参数生成ConInfoRecord 合同履约流水信息
     *
     * @param performanceVO 页面传递登记信息
     * @param optrate       保存或提交标志
     * @param userVo        操作用户
     * @return ConInfoRecord
     */
    protected ConInfoRecord getConInfoRecord(ConPerformanceVO performanceVO, String optrate, ICurrentUser userVo) {
        ConInfoRecord entity = new ConInfoRecord();
        entity.setMid(performanceVO.getMid());// 主信息ID
        entity.setAttachmentId(performanceVO.getAttachmentId());
        entity.setBusType(ConConstant.CON_BUS_TYPE_05);// 履约中
        entity.setValidSign(ConConstant.CON_VALID_SIGN_0);// 业务中
        entity.setStatus(ConConstant.CON_DRAFT_STATUS_0);// 暂存
        entity.setOpMsg(performanceVO.getOpMsg());// 履约原因
        entity.setOpDate(performanceVO.getOpDate());// 履约时间
        entity.setOpNo(performanceVO.getOpNo());// 履约申请编号
        entity.setOpMoney(performanceVO.getOpMoney());// 履约金额
        // entity.setRmk(performanceVO.getRmk());//备注
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
